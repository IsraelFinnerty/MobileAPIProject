package com.wit.venues.models.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.wit.venues.helpers.readImageFromPath
import com.wit.venues.models.VenueModel
import com.wit.venues.models.VenueStore
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class VenueFireStore(val context: Context) : VenueStore, AnkoLogger {

    val venues = ArrayList<VenueModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override fun findAll(): List<VenueModel> {
        return venues
    }

    override fun findById(id: Long): VenueModel? {
        val foundVenue: VenueModel? = venues.find { p -> p.id == id }
        return foundVenue
    }

    override fun create(venue: VenueModel) {
        db = FirebaseDatabase.getInstance().reference
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        st = FirebaseStorage.getInstance().reference
        val key = db.child("users").child(userId).child("venues").push().key
        key?.let {
            venue.fbId = key
            venues.add(venue)
            db.child("users").child(userId).child("venues").child(key).setValue(venue)
        }
        updateImage(venue)
    }

    override fun update(venue: VenueModel) {
        var foundVenue: VenueModel? = venues.find { p -> p.fbId == venue.fbId }
        if (foundVenue != null) {
            foundVenue.name = venue.name
            foundVenue.description = venue.description
            foundVenue.image1 = venue.image1
            foundVenue.image2 = venue.image2
            foundVenue.image3 = venue.image3
            foundVenue.image4 = venue.image4
            foundVenue.lat = venue.lat
            foundVenue.lng = venue.lng
            foundVenue.zoom = venue.zoom
            foundVenue.notes = venue.notes
            foundVenue.visited = venue.visited
            foundVenue.dateVisitedYear = venue.dateVisitedYear
            foundVenue.dateVisitedMonth = venue.dateVisitedMonth
            foundVenue.dateVisitedDay = venue.dateVisitedDay
        }

        db.child("users").child(userId).child("venues").child(venue.fbId).setValue(venue)
        if ((venue.image1.length) > 0 || (venue.image2.length) > 0  || (venue.image3.length) > 0 || (venue.image4.length) > 0) {
            updateImage(venue)
        }
    }



    override fun clear() {
        venues.clear()
    }

    override fun delete(venue: VenueModel) {
        db.child("users").child(userId).child("venues").child(venue.fbId).removeValue()
        venues.remove(venue)
    }

    fun fetchVenues(venuesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(venues) { it.getValue<VenueModel>(VenueModel::class.java) }
                venuesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        venues.clear()
        db.child("users").child(userId).child("venues").addListenerForSingleValueEvent(valueEventListener)
    }


    override fun seed() {
        val venue1 = VenueModel(
            name = "Downeen"
            ,description = "This coastal stack is situated at Carrigagappul Cove, c. 2km S of Rosscarbery on the SW coast of Co. Cork. Depicted on the first edition OS six-inch map as site of Donoure Castle (1842). The site can be described as a small rocky island projecting SE into Castle Bay at an altitude of 9m OD with Downeen Castle (CO143-069002) at its N edge. There is no surface trace of earlier defences. Westropp (1914, 112) considered island 'only reached by a plank even when used as a dun in its fort-days'."
            ,lat = 51.559961
            ,lng = -9.024633
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fdowneen.jpg?alt=media&token=68d1310e-b9cc-4a29-b98d-a4a6a56d8165"
            ,image2 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fdowneenmap.png?alt=media&token=b359d0da-39db-4dae-876d-266f35d29ea2"
            ,image3 = ""
            ,image4 = ""

        )
        create(venue1)

        val venue2 = VenueModel(
            name = "Dundeady"
            ,description = "This headland is situated at Galley Head, c. 4km SW of Ardfield village on the SW coast of Co. Cork. Depicted as 'Dundeady Head' on the first edition OS six-inch map (1842). It can be described as an irregular promontory projecting S into the Atlantic at an altitude of 30m OD. There are no obvious remains of defences earlier than the medieval fortifications which cross neck of Dundeady (or Galley) Head for c. 95m. That said, Westropp (1914, 144) suggests these might mask earlier ones. For example, a Dip in front (to N) of the curtain wall has ditch-like appearance, though it is probably a natural feature (Power et al. 1992, 130). A possible standing stone is recorded near the centre of the headland at its highest point. No other potential early features such as hut-sites are recorded. A modern lighthouse is situated at the seaward end of the headland. Site is currently under pasture."
            ,lat = 51.531901
            ,lng = -8.953403
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fdundeady.jpg?alt=media&token=f01a4550-be58-493b-bb94-44ab31a94099"
            ,image2 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fdundeadymap.png?alt=media&token=6f2a5ac6-80e6-4d7a-b4f6-d086bce6136b"
            ,image3 = ""
            ,image4 = ""

        )
        create(venue2)


        val venue3 = VenueModel(
            name = "Donoure"
            ,description = "This headland is situated at Bealcoon Cove, c. 4km E of Owenahinchy (Rosscarbery) on the SW coast of Co. Cork. The site is known as 'Dunoure' which Windele gives as 'Dun uair' (Westropp 1914, 105). It can be described as a narrow finger, the upper surface extending to no more than 0.1ha, projecting SW into Bealacoon Bay at an altitude of 4m OD. It is joined to mainland by narrow neck with V-shaped profile and is depicted on the first edition OS six-inch map as site of Donoure Castle (1842). Traces of Late Medieval defences are recorded on seaward side of the neck. There are no visible traces of earlier defences."
            ,lat = 51.543887
            ,lng = -8.956465
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fdonoure.png?alt=media&token=5e6f1cfd-c7b3-4495-a25a-82d723c43b9b"
            ,image2 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fdonouremap.png?alt=media&token=67377583-3802-48ef-87bc-cf9db92921ea"
            ,image3 = ""
            ,image4 = ""
        )
        create(venue3)

        val venue4 = VenueModel(
            name = "Reenogrena"
            ,description = "This promontory is situated at Siege Cove, c.3km SE of Glandore in West Cork. The headland can be described as a small rectangular area (L 70m; Wth 22m) projecting SE over Siege Cove at an altitude of 25m OD. Known locally as \\\"The Caisleanin\\\", it is not depicted as a defended headland on any edition of the OS six-inch map. The narrow neck of the promontory on the W is defended by a rock-cut ditch measuring 4m in width and 2m in depth. A field fence is recorded on the landward side. A stone built wall is recorded (H 1m) on N cliff-edge. There are no formal entrances into the the promontory. The E and W ends of the interior are divided by a natural fault lying N and S. A grass covered depression on the seaward side is interpreted as a possible infilled well (Power et al. 1992, 129) while a further a circular depression (diam. 6m; D 0.8m) and a sod covered arc of bank (diam. 2.6; H 0.3m) may be the remains of a possible hut-site. The site is currently much overgrown."
            ,lat = 51.548237
            ,lng = -9.074029
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Freenogrena.png?alt=media&token=a417d112-2f16-44e1-8371-768aeb8e2d5c"
            ,image2 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Freenogrenamap.png?alt=media&token=6db7b65e-310a-42ed-a540-9782959269a5"
            ,image3 = ""
            ,image4 = ""
        )
        create(venue4)

        val venue5 = VenueModel(
            name = "Carrigillihy"
            ,description = "The site is located on the western shore of Glandore Harbour, West Cork. It can be described as a rectangular area of approximately 0.4ha projecting E into the harbour at an altitude of 10m OD. The enclosing elements are composed of a single bank and ditch on the W side with evidence of a small outer counterscarp bank. There is a single break that is probably of later date. Westropp describes that the bank was overlain with a modern fence in the early twentieth century (1914, 95). There are no recorded internal features such as hut-sites and excavation in 1952 did not find any trace of occupation in an 'archaeologically sterile' interior (O'Kelly 1952). Sections through the banks of the promontory revealed dry-stone faced bank with a core of stone rubble quarried from a steep-sided ditch. The bank also had evidence of an internal terrace. The original entrance was not identified with the excavator suggesting it may have been eroded away by coastal erosion (ibid). The site is currently under pasture."
            ,lat = 51.540364
            ,lng = -9.1114
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fcarrigillihy.jpg?alt=media&token=50bbc967-2e8c-49cf-9626-93218a428a1f"
            ,image2 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fcarrigillihymap.png?alt=media&token=9086f6a2-60a2-4c13-9713-7704796ce7f4"
            ,image3 = ""
            ,image4 = ""
        )
        create(venue5)

        val venue6 = VenueModel(
            name = "Moyross"
            ,description = "The headland is located c. 2.1km SE of Castletownshend Village in West Cork. It is situated in rough pasture, on a promontory projecting E at an altitude of 12m OD, with high cliffs on three sides, and with views S to High Island and Low Island. Near its W end, the promontory is defended by the remains of an earthen bank (L 6m N-S; Wth 2.1m; H 0.55m) both ends of which are being eroded by the sea. There is an external ditch (L 6m N-S; Wth 1.8m; D 0.2m) as well as traces of a possible internal example (L 6m N-S; Wth 1m; D 0.1m). The interior narrows and slopes gently to the E for a distance of c. 25m. There are no recorded internal features within the enclosed area of c.0.02ha."
            ,lat = 51.522688
            ,lng = -9.143409
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fmoyross.png?alt=media&token=094a0d1c-f3d7-4f78-ad54-9fcdf3672943"
            ,image2 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fmoyrossmap.png?alt=media&token=ce95fa62-915c-417a-a60c-7e4a97e4b6d5"
            ,image3 = ""
            ,image4 = ""
        )
        create(venue6)

        val venue7 = VenueModel(
            name = "Reen"
            ,description = "This promontory is situated at Reen Point, immediately W of Skiddy Island and 1km S of Castletownsend on the W coast of Co. Cork. The headland can be defined as a small rectangular area projecting SW into Castlehaven Bay. Westropp (1914, 99) noted at the neck, 'two very shallow straight fosses with an intervening wall and an inner one revetting a mound'. He describes the outer ditch as a 'shallow hollow' and the next wall outside as probably recent. Defences remain as in Westropp's time. Both inner (H 1.35m) and outer (H 1.2m) banks are earthen while the inner ditch is completely infilled. The outer example is extant measuring 0.9m in depth and is crossed by a causeway associated with a central gap through both banks. Interior is grass, heather and gorse over rock outcropping. A battery-operated beacon is located on seaward edge."
            ,lat = 51.516471
            ,lng = -9.174795
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Freen.png?alt=media&token=a51231c4-1b0f-47af-9154-a9d46065536d"
            ,image2 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Freenmap.png?alt=media&token=4bc965d4-1158-4663-9921-fef818858882"
            ,image3 = ""
            ,image4 = ""
        )
        create(venue7)

        val venue8 = VenueModel(
            name = "Portadoona"
            ,description = "The site is located c. 5km SE of Skibereen town, West Cork. It is composed of an irregular, shallow promontory of approximately 0.11ha that projects E into the Atlantic at an altitude of 24m OD. Situated on elevated land-mass of which Toehead marks the S point. The only approach to the site is from the landward side on the W, where the bank and ditch are well preserved, composed of a single curvilinear rampart with a terraced inner face. There was no evidence of an outer stone facing. The ditch was rock-cut and flat bottomed. There is a single break that is probably of later date. Westropp describes the site as a 'small headland defended by a curved fosse and mound about 120 feet long, on grassy cliffs in Scobaun townland' (1914, 95). No evidence of any internal features on the surface. Excavation of the interior did reveal two hearth sites and a single post-hole (O'Kelly 1952, 26-9). Promontory now covered by rough grazing; both seaward edges of defences suffering from erosion."
            ,lat = 51.500926
            ,lng = -9.212005
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fportadoona.png?alt=media&token=888d82d8-140e-443b-a427-88929c082cf6"
            ,image2 = "https://firebasestorage.googleapis.com/v0/b/venues-2a0b2.appspot.com/o/Seed%2Fportadoonamap.png?alt=media&token=13a0a56a-8ec4-4cc9-a6f1-3d27a18bf7e5"
            ,image3 = ""
            ,image4 = ""
        )
        create(venue8)

    }

    fun generateRandomId(): Long {
        return Random().nextLong()
    }


    fun updateImage(venue: VenueModel) {
        if (venue.image1 != "") {
              val fileName = File(venue.image1)
              val imageName = fileName.getName()

                    var imageRef = st.child(userId + '/' + imageName)
                    val baos = ByteArrayOutputStream()
                    val bitmap = readImageFromPath(context, venue.image1)

                    bitmap?.let {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()
                        val uploadTask = imageRef.putBytes(data)
                        uploadTask.addOnFailureListener {
                            println(it.message)
                        }.addOnSuccessListener { taskSnapshot ->
                            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                                venue.image1 = it.toString()
                                db.child("users").child(userId).child("venues")
                                    .child(venue.fbId).setValue(venue)
                            }
                        }
                    }
                }

        if (venue.image2 != "") {
            val fileName = File(venue.image2)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, venue.image2)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        venue.image2 = it.toString()
                        db.child("users").child(userId).child("venues")
                            .child(venue.fbId).setValue(venue)
                    }
                }
            }
        }

        if (venue.image3 != "") {
            val fileName = File(venue.image3)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, venue.image3)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        venue.image3 = it.toString()
                        db.child("users").child(userId).child("venues")
                            .child(venue.fbId).setValue(venue)
                    }
                }
            }
        }

        if (venue.image4 != "") {
            val fileName = File(venue.image4)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, venue.image4)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        venue.image4 = it.toString()
                        db.child("users").child(userId).child("venues")
                            .child(venue.fbId).setValue(venue)
                    }
                }
            }
        }

    }
}