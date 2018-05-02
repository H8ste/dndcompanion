package nolse16.dndcompanion

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
//import android.immersivemode


import kotlinx.android.synthetic.main.content_mainscreen.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import java.io.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pub.devrel.easypermissions.EasyPermissions
import com.google.gson.annotations.SerializedName

data class Topic(
        @SerializedName("strengthArr") var strengthArr: IntArray,
        @SerializedName("dexterityArr") var dexterityArr: IntArray,
        @SerializedName("constitutionArr") var constitutionArr: IntArray,
        @SerializedName("IntelligenceArr") var intelligenceArr: IntArray,
        @SerializedName("wisdom") var wisdomArr: IntArray,
        @SerializedName("charismaArr") var charismaArr: IntArray,
        @SerializedName("armorClass") var armorClass: Int,
        @SerializedName("initiative") var initiative: Int,
        @SerializedName("speed") var speed: Int,
        @SerializedName("hitPointsMax") var hitPointsMax: Int,
        @SerializedName("hitPointsCurrent") var hitPointsCurrent: Int,
        @SerializedName("tempHitPoints") var tempHitPoints: Int,
        @SerializedName("proficiency") var proficiency: Int,
        @SerializedName("experience") var experience: Int,
        @SerializedName("level") var level: Int,
        @SerializedName("gold") var gold: IntArray,
        @SerializedName("deathSavesSucesses") var deathSavesSuccesses: Int,
        @SerializedName("deathSavesFailures") var deathSavesFailures: Int
)


class Mainscreen : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val decorView = window.decorView
        decorView.systemUiVisibility = 0
        setContentView(R.layout.activity_mainscreen)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onStart() {
        super.onStart()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)
        } else {
//            var mew = readFromFile(applicationContext)

            var topic = Topic(
                    intArrayOf(0,10),
                    intArrayOf(4,18),
                    intArrayOf(1,12),
                    intArrayOf(1,12),
                    intArrayOf(2, 15),
                    intArrayOf(1, 12),
                    11, 3, 25, 27, 27, 0,
                    2, 5200, 4, intArrayOf(142, 134, 94, 6), 0, 0
                    )
            writeToFile(topic)
            topic = readFromFile()
            armorClassText.text = topic.armorClass.toString()
            hitpointsText.text = topic.hitPointsCurrent.toString()
            temphitpointsText.text = topic.tempHitPoints.toString()
            speedText.text = topic.speed.toString() + " feet"
        }

    }
    private fun writeToFile(data: Topic) {

        try {
            val gson = GsonBuilder().create()
            val filename = "Testing.json"
            val path = Environment.getExternalStorageDirectory().absolutePath + "/" + filename
            val file = File(path)
            val fileWriter = FileWriter(file)
            gson.toJson(data, fileWriter)
            fileWriter.close()
            Log.e("Debug", "File write Succeded")
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }

    }

    private fun readFromFile() : Topic{

        val gson = Gson()

//        try {
            FileReader(Environment.getExternalStorageDirectory().absolutePath + "/" + "Testing.json").use { reader ->

                // Convert JSON to Java Object
                var topic = gson.fromJson(reader, Topic::class.java)
                Log.e("Debug", "jsonFile Read succesfully")
                return topic
            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_mainscreen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        //var mew = Player()
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
        const val REQUEST_PERMISSION = 1
    }



}
