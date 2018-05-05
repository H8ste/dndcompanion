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

data class Player(
        @SerializedName("attributeRoles") var attributeRoles: IntArray,
//        @SerializedName("dexterityArr") var dexterityArr: IntArray,
//        @SerializedName("constitutionArr") var constitutionArr: IntArray,
//        @SerializedName("IntelligenceArr") var intelligenceArr: IntArray,
//        @SerializedName("wisdom") var wisdomArr: IntArray,
//        @SerializedName("charismaArr") var charismaArr: IntArray,
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
        @SerializedName("deathSavesFailures") var deathSavesFailures: Int,
        @SerializedName("SavThrowsChosen") var savThrowsChosen: BooleanArray,
        @SerializedName("SkillsProf") var skillsProf: BooleanArray,
        @SerializedName("SkillsProfExtraBoost") var  skillsProfExtra: BooleanArray
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
        val nAthletics = 0; val nAcrobatics = 1; val nSleightOfHand = 2; val nStealth = 3
        val nArcana = 4; val nHistory = 5; val nInvestigation = 6; val nNature = 7
        val nReligion = 8; val nAnimalHandling = 9; val nInsight = 10; val nMedicine = 11
        val nPerception = 12; val nSurvival = 13; val nDeception = 14; val nIntimidation = 15
        val nPerformance = 16; val nPersuasion = 17

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)
        } else {
//            var mew = readFromFile(applicationContext)

            var topic = Player(
                    //Strength, Dexterity, Constituion, Intelligence, Wisdom, Charisma
                    intArrayOf(10,18,12,12,15,12),
//                    intArrayOf(4,18),
//                    intArrayOf(1,12),
//                    intArrayOf(1,12),
//                    intArrayOf(2, 15),
//                    intArrayOf(1, 12),
                    11, 3, 25, 27, 27, 0,
                    2, 5200, 4, intArrayOf(142, 134, 94, 6), 0,
                    0,
                    booleanArrayOf(false,true,false,true,false,false) //str, dex, cons, int, wis, cha,
                    , booleanArrayOf(false,true,true,true,false,false,false,false,false,false,true,false,false,false,false,false,false,false),
                    booleanArrayOf(false,true,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false)
                    )
            writeToFile(topic)
            topic = readFromFile()
            armorClassText.text = topic.armorClass.toString()
            hitpointsText.text = topic.hitPointsCurrent.toString()
            temphitpointsText.text = topic.tempHitPoints.toString()
            speedText.text = topic.speed.toString() + " feet"
            StrMod.text = "+" + calcAttributeMod(topic.attributeRoles[0]).toString()
            DexMod.text = "+" + calcAttributeMod(topic.attributeRoles[1]).toString()
            ConMod.text = "+" + calcAttributeMod(topic.attributeRoles[2]).toString()
            IntMod.text = "+" + calcAttributeMod(topic.attributeRoles[3]).toString()
            WisMod.text = "+" + calcAttributeMod(topic.attributeRoles[4]).toString()
            ChaMod.text = "+" + calcAttributeMod(topic.attributeRoles[5]).toString()

            StrSavingThrow.text = (calcAttributeMod(topic.attributeRoles[0]) + addPotentialProf(topic, 0)).toString()
            DexSavingThrow.text = (calcAttributeMod(topic.attributeRoles[1]) + addPotentialProf(topic, 1)).toString()
            ConSavingThrow.text = (calcAttributeMod(topic.attributeRoles[2]) + addPotentialProf(topic, 2)).toString()
            IntSavingThrow.text = (calcAttributeMod(topic.attributeRoles[3]) + addPotentialProf(topic, 3)).toString()
            WisSavingThrow.text = (calcAttributeMod(topic.attributeRoles[4]) + addPotentialProf(topic, 4)).toString()
            ChaSavingThrow.text = (calcAttributeMod(topic.attributeRoles[5]) + addPotentialProf(topic, 5)).toString()

            Athletics.text = (calcAttributeMod(topic.attributeRoles[0]) + addPotentialSkillBoost(topic, nAthletics)).toString()

            Acrobatics.text = (calcAttributeMod(topic.attributeRoles[1]) + addPotentialSkillBoost(topic, nAcrobatics)).toString()
            SleightOfHand.text = (calcAttributeMod(topic.attributeRoles[1]) + addPotentialSkillBoost(topic, nSleightOfHand)).toString()
            Stealth.text = (calcAttributeMod(topic.attributeRoles[1]) + addPotentialSkillBoost(topic, nStealth)).toString()



            Arcana.text = (calcAttributeMod(topic.attributeRoles[3]) + addPotentialSkillBoost(topic, nArcana)).toString()
            History.text = (calcAttributeMod(topic.attributeRoles[3]) + addPotentialSkillBoost(topic, nHistory)).toString()
            Investigation.text = (calcAttributeMod(topic.attributeRoles[3]) + addPotentialSkillBoost(topic, nInvestigation)).toString()
            Nature.text = (calcAttributeMod(topic.attributeRoles[3]) + addPotentialSkillBoost(topic, nNature)).toString()
            Religion.text = (calcAttributeMod(topic.attributeRoles[3]) + addPotentialSkillBoost(topic, nReligion)).toString()




            AnimalHandling.text = (calcAttributeMod(topic.attributeRoles[4]) + addPotentialSkillBoost(topic, nAnimalHandling)).toString()
            Insight.text = (calcAttributeMod(topic.attributeRoles[4]) + addPotentialSkillBoost(topic, nInsight)).toString()
            Medicine.text = (calcAttributeMod(topic.attributeRoles[4]) + addPotentialSkillBoost(topic, nMedicine)).toString()
            Perception.text = (calcAttributeMod(topic.attributeRoles[4]) + addPotentialSkillBoost(topic, nPerception)).toString()
            Survival.text = (calcAttributeMod(topic.attributeRoles[4]) + addPotentialSkillBoost(topic, nSurvival)).toString()



            Deception.text = (calcAttributeMod(topic.attributeRoles[5]) + addPotentialSkillBoost(topic, nDeception)).toString()
            Intimidation.text = (calcAttributeMod(topic.attributeRoles[5]) + addPotentialSkillBoost(topic, nIntimidation)).toString()
            Performance.text = (calcAttributeMod(topic.attributeRoles[5]) + addPotentialSkillBoost(topic, nPerformance)).toString()
            Persuasion.text = (calcAttributeMod(topic.attributeRoles[5]) + addPotentialSkillBoost(topic, nPersuasion)).toString()


//            Log.e("debug", (calcAttributeMod(topic.attributeRoles[0]) + topic.proficiency).toString())
        }

    }

    private fun calcAttributeMod(diceThrow: Int): Int{
        when (diceThrow) {
            8,9 -> return -1
            10,11 -> return 0
            12,13 -> return 1
            14,15 -> return 2
            16,17 -> return 3
            18,19 -> return 4
            20 -> return 5
        }
        return 0
    }
    private fun addPotentialProf(player: Player, attribute: Int) : Int{
        if(player.savThrowsChosen[attribute] == true)
        return player.proficiency
        return 0
    }

    private fun addPotentialSkillBoost(player: Player, index: Int) : Int{
        if (player.skillsProf[index] == true) {
            if (player.skillsProfExtra[index] == true) {
                return player.proficiency * 2
            }
            return player.proficiency
        }
        return 0
    }

    private fun writeToFile(data: Player) {

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

    private fun readFromFile() : Player{

        val gson = Gson()

//        try {
            FileReader(Environment.getExternalStorageDirectory().absolutePath + "/" + "Testing.json").use { reader ->

                // Convert JSON to Java Object
                var topic = gson.fromJson(reader, Player::class.java)
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
