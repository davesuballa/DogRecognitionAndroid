package com.example.buddy

import android.app.ActivityOptions
import android.app.AlertDialog
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import androidx.core.app.ActivityOptionsCompat
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// Activity
class dog_avatar_creation : AppCompatActivity(),
    FirstFragment.OnBreedSelectedListener,
    SecondFragment.OnFurColorSelectedListener,
    thirdfragment.OnAccessorySelectedListener {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter
    private  var selectedBreed: String  = ""
    private  var selectedFurColor: String  = ""
    private lateinit var tabitembackviewbreed: TextView
    private lateinit var tabitemfurcolor: TextView
    private lateinit var tabitemaccessory: TextView
    private lateinit var doneButton : Button
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dog_avatar_creation)


        database = FirebaseDatabase.getInstance().getReference("Users")


        tabLayout = findViewById(R.id.tablyt)
        viewPager2 = findViewById(R.id.viewPager2)
        tabitembackviewbreed = findViewById(R.id.breed)
        tabitemfurcolor = findViewById(R.id.furtab)
        tabitemaccessory = findViewById(R.id.accessory)
        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(FirstFragment())
        adapter.addFragment(SecondFragment())
        adapter.addFragment(thirdfragment())
        viewPager2.adapter = adapter
        val transparentDrawable = ColorDrawable(Color.TRANSPARENT)
        tabLayout.setSelectedTabIndicator(transparentDrawable)
        tabLayout.isTabIndicatorFullWidth = false

        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            tab?.view?.setBackgroundResource(android.R.color.transparent)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {

                    when (it.position) {
                        // Handle the first tab item
                        0 -> {
                            // Set the background of your TextView based on the selected tab
                            tabitembackviewbreed.setBackgroundResource(R.drawable.breed_2)

                        }
                        // Handle other tab items as needed
                        1 -> {
                            // Set the background of your TextView based on the selected tab
                            tabitemfurcolor.setBackgroundResource(R.drawable.fur_color_2)
                        }

                        2 -> {
                            // Set the background of your TextView based on the selected tab
                            tabitemaccessory.setBackgroundResource(R.drawable.accessories_2)
                        }
                    }
                    viewPager2.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    // Restore the background of breed TextView
                    0 -> {
                        tabitembackviewbreed.setBackgroundResource(R.drawable.breed)
                   }

                    // Restore the background of fur color TextView
                    1 -> {
                        tabitemfurcolor.setBackgroundResource(R.drawable.fur_color)

                    }
                    // Restore the background of accessory TextView
                    2 -> {
                        tabitemaccessory.setBackgroundResource(R.drawable.accessories)

                    }

                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })

        doneButton = findViewById(R.id.buttonskip)
        // Set OnClickListener for the done button
        doneButton.setOnClickListener {
            // Check if breed is not empty but fur color is empty
            if (!selectedBreed.isNullOrEmpty() && selectedFurColor.isNullOrEmpty()) {
                // Display a toast message indicating that the user should select a fur color
                Toast.makeText(this@dog_avatar_creation, "Please select a fur color", Toast.LENGTH_SHORT).show()
            } else if (selectedBreed.isNullOrEmpty() && selectedFurColor.isNullOrEmpty() && selectedAccessory.isNullOrEmpty()) {
                // Check if all required fields (breed, fur color, accessory) are empty or null
                // Display a toast message indicating that the user should select a breed
                Toast.makeText(this@dog_avatar_creation, "Please select a breed", Toast.LENGTH_SHORT).show()
            } else {
                val alertDialogBuilder = AlertDialog.Builder(this@dog_avatar_creation)

                alertDialogBuilder.setMessage("Are you done customizing your dog's avatar?")
                alertDialogBuilder.setCancelable(false)

                alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
                    Toast.makeText(this@dog_avatar_creation, "Dog Profile has been added.", Toast.LENGTH_SHORT).show()
                    updateUserDetails(selectedBreed, selectedFurColor, selectedAccessory)
                    val intent = Intent(this@dog_avatar_creation, homepageold::class.java)
                    val options = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.dissolve_in, // Slide in from right
                        R.anim.dissolve_out // Slide out to left
                    )
                    startActivity(intent, options.toBundle())
                    finish()
                    dialog.dismiss()
                }

                alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                    Toast.makeText(this@dog_avatar_creation, "Changing Dog Avatar is cancelled", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }

                // Create and show the AlertDialog
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }




    }





    private fun updateUserDetails(selectedBreed: String, selectedFurColor: String, selectedAccessory: String?) {


        // Get the current user's ID from Firebase Authentication
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserID != null) {
            val userRef = database.child(currentUserID)
            getCurrentDogId(userRef) { currentDogId ->
                if (currentDogId != null) {
                    val userDogsRef = userRef.child("Dogs").child(currentDogId)

                    // Create a map to hold the updated values
                    val updates = HashMap<String, Any>()
                    updates["breedName"] = selectedBreed
                    updates["furColor"] = selectedFurColor
                    selectedAccessory?.let {
                        updates["accessory"] = it
                    }

                    // Update the user's details with the selected dog
                    userDogsRef.updateChildren(updates)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Dog details have been updated.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, homepageold::class.java)
                            val options = ActivityOptions.makeCustomAnimation(
                                this,
                                R.anim.dissolve_in, // Slide in from right
                                R.anim.dissolve_out // Slide out to left
                            )
                            startActivity(intent, options.toBundle())
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to update dog details.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "No dog ID found", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getCurrentDogId(userRef: DatabaseReference, onComplete: (String?) -> Unit) {
        // Query to fetch the current dog ID
        userRef.child("Dogs").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentDogId: String? = null
                snapshot.children.lastOrNull()?.let { lastChild ->
                    currentDogId = lastChild.key
                }
                onComplete(currentDogId)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                onComplete(null)
            }
        })
    }



    private fun ToNextActivity(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_right, // Slide in from right
            R.anim.slide_out_left // Slide out to left
        )
        startActivity(intent, options.toBundle())
    }

    private val breedMap = mapOf(
        R.id.gretriever to "Golden Retriever",
        R.id.shitzu to "Shih Tzu",
        R.id.poodle to "Poodle",
        R.id.chihuahua to "Chihuahua",
        R.id.pomeranian to "Pomeranian"
    )

    override fun onBreedSelected(breedId: Int) {
        val gretrieverView: View = findViewById(R.id.gretriever)
        val shitzuView: View = findViewById(R.id.shitzu)
        val poodleView: View = findViewById(R.id.poodle)
        val chihuahuaView: View = findViewById(R.id.chihuahua)
        val pomeranianView: View = findViewById(R.id.pomeranian)
        gretrieverView.visibility = View.INVISIBLE
        shitzuView.visibility = View.INVISIBLE
        poodleView.visibility = View.INVISIBLE
        chihuahuaView.visibility = View.INVISIBLE
        pomeranianView.visibility = View.INVISIBLE

        val selectedBreedName = breedMap[breedId]
        selectedBreedName?.let {
            findViewById<View>(breedId).visibility = View.VISIBLE
            selectedBreed = it
        }
    }

        private val furColorMap = mapOf(
        R.id.fur1btn to "Fur1",
        R.id.fur2btn to "Fur2",
        R.id.fur3btn to "Fur3",
            R.id.fur4btn to "Fur4",
            R.id.fur5btn to "Fur5",
            R.id.fur6btn to "Fur6",
            R.id.fur7btn to "Fur7",
    )

    override fun onFurColorSelected(furColorId: Int) {
        val selectedFurColorName = furColorMap[furColorId]
        selectedFurColorName?.let {
            updateBreedFurColorView(selectedBreed, it)
            selectedFurColor = it
        }
    }

    private fun getDrawableResourceId(breed: String, furColorName: String): Int {
        // Return the resource ID of the drawable based on the breed and fur color name
        return when (breed) {
            "Golden Retriever" -> {
                val gretriever2 = findViewById<TextView>(R.id.fur2txt)
                val gretriever1 = findViewById<TextView>(R.id.fur1txt)
                val gretriever3 = findViewById<TextView>(R.id.fur3txt)
                val gretriever4 = findViewById<TextView>(R.id.fur4txt)
                val gretriever5 = findViewById<TextView>(R.id.fur5txt)
                val gretriever6 = findViewById<TextView>(R.id.fur6txt)
                when (furColorName) {
                    "Fur1" -> {
                        gretriever1?.setBackgroundResource(R.drawable.goldenc1_1)
                        gretriever2?.setBackgroundResource(R.drawable.goldenc2)
                        gretriever3?.setBackgroundResource(R.drawable.goldenc3)
                        gretriever4?.setBackgroundResource(R.drawable.goldenc4)
                        gretriever5?.setBackgroundResource(R.drawable.goldenc5)
                        gretriever6?.setBackgroundResource(R.drawable.goldenc6)
                        R.drawable.golden1
                    }
                    "Fur2" -> {
                        gretriever2?.setBackgroundResource(R.drawable.goldenc2_1)
                        gretriever1?.setBackgroundResource(R.drawable.goldenc1)
                        gretriever3?.setBackgroundResource(R.drawable.goldenc3)
                        gretriever4?.setBackgroundResource(R.drawable.goldenc4)
                        gretriever5?.setBackgroundResource(R.drawable.goldenc5)
                        gretriever6?.setBackgroundResource(R.drawable.goldenc6)
                        R.drawable.golden2
                    }
                    "Fur3" ->{
                        gretriever1?.setBackgroundResource(R.drawable.goldenc1)
                        gretriever2?.setBackgroundResource(R.drawable.goldenc2)
                        gretriever3?.setBackgroundResource(R.drawable.goldenc3_1)
                        gretriever4?.setBackgroundResource(R.drawable.goldenc4)
                        gretriever5?.setBackgroundResource(R.drawable.goldenc5)
                        gretriever6?.setBackgroundResource(R.drawable.goldenc6)

                        R.drawable.golden3
                    }
                    "Fur4" -> {
                        gretriever1?.setBackgroundResource(R.drawable.goldenc1)
                        gretriever2?.setBackgroundResource(R.drawable.goldenc2)
                        gretriever3?.setBackgroundResource(R.drawable.goldenc3)
                        gretriever4?.setBackgroundResource(R.drawable.goldenc4_1)
                        gretriever5?.setBackgroundResource(R.drawable.goldenc5)
                        gretriever6?.setBackgroundResource(R.drawable.goldenc6)
                        R.drawable.golden4
                    }
                    "Fur5" -> {
                        gretriever1?.setBackgroundResource(R.drawable.goldenc1)
                        gretriever2?.setBackgroundResource(R.drawable.goldenc2)
                        gretriever3?.setBackgroundResource(R.drawable.goldenc3)
                        gretriever4?.setBackgroundResource(R.drawable.goldenc4)
                        gretriever5?.setBackgroundResource(R.drawable.goldenc5_1)
                        gretriever6?.setBackgroundResource(R.drawable.goldenc6)
                        R.drawable.golden5
                    }
                    "Fur6" -> {
                        gretriever1?.setBackgroundResource(R.drawable.goldenc1)
                        gretriever2?.setBackgroundResource(R.drawable.goldenc2)
                        gretriever3?.setBackgroundResource(R.drawable.goldenc3)
                        gretriever4?.setBackgroundResource(R.drawable.goldenc4)
                        gretriever5?.setBackgroundResource(R.drawable.goldenc5)
                        gretriever6?.setBackgroundResource(R.drawable.goldenc6_1)
                        R.drawable.golden6
                    }
                    else -> throw IllegalArgumentException("Unknown fur color for Golden Retriever: $furColorName")
                }
            }
            "Shih Tzu" -> {
                val shitzu1 = findViewById<TextView>(R.id.fur1txt)
                val shitzu2 = findViewById<TextView>(R.id.fur2txt)
                val shitzu3 = findViewById<TextView>(R.id.fur3txt)
                val shitzu4 = findViewById<TextView>(R.id.fur4txt)
                val shitzu5 = findViewById<TextView>(R.id.fur5txt)
                val shitzu6 = findViewById<TextView>(R.id.fur6txt)
                val shitzu7 = findViewById<TextView>(R.id.fur7txt)
                when (furColorName) {

                    "Fur1" -> {
                        shitzu1?.setBackgroundResource(R.drawable.shihtzuc2_1)
                        shitzu2?.setBackgroundResource(R.drawable.shihtzuc3)
                        shitzu3?.setBackgroundResource(R.drawable.shihtzuc1)
                        shitzu4?.setBackgroundResource(R.drawable.shihtzuc5)
                        shitzu5?.setBackgroundResource(R.drawable.shihtzuc7)
                        shitzu6?.setBackgroundResource(R.drawable.shihtzuc6)
                        shitzu7?.setBackgroundResource(R.drawable.shihtzuc4)
                        R.drawable.shitzu7
                    }
                    "Fur2" -> {
                        shitzu1?.setBackgroundResource(R.drawable.shihtzuc2)
                        shitzu2?.setBackgroundResource(R.drawable.shihtzuc3_1)
                        shitzu3?.setBackgroundResource(R.drawable.shihtzuc1)
                        shitzu4?.setBackgroundResource(R.drawable.shihtzuc5)
                        shitzu5?.setBackgroundResource(R.drawable.shihtzuc7)
                        shitzu6?.setBackgroundResource(R.drawable.shihtzuc6)
                        shitzu7?.setBackgroundResource(R.drawable.shihtzuc4)
                        R.drawable.shitzu5}
                    "Fur3" -> {
                        shitzu1?.setBackgroundResource(R.drawable.shihtzuc2)
                        shitzu2?.setBackgroundResource(R.drawable.shihtzuc3)
                        shitzu3?.setBackgroundResource(R.drawable.shihtzuc1_1)
                        shitzu4?.setBackgroundResource(R.drawable.shihtzuc5)
                        shitzu5?.setBackgroundResource(R.drawable.shihtzuc7)
                        shitzu6?.setBackgroundResource(R.drawable.shihtzuc6)
                        shitzu7?.setBackgroundResource(R.drawable.shihtzuc4)
                        R.drawable.shitzu6
                    }
                    "Fur4" -> {
                        shitzu1?.setBackgroundResource(R.drawable.shihtzuc2)
                        shitzu2?.setBackgroundResource(R.drawable.shihtzuc3)
                        shitzu3?.setBackgroundResource(R.drawable.shihtzuc1)
                        shitzu4?.setBackgroundResource(R.drawable.shihtzuc5_1)
                        shitzu5?.setBackgroundResource(R.drawable.shihtzuc7)
                        shitzu6?.setBackgroundResource(R.drawable.shihtzuc6)
                        shitzu7?.setBackgroundResource(R.drawable.shihtzuc4)
                        R.drawable.shitzu1}
                    "Fur5" -> {
                        shitzu1?.setBackgroundResource(R.drawable.shihtzuc2)
                        shitzu2?.setBackgroundResource(R.drawable.shihtzuc3)
                        shitzu3?.setBackgroundResource(R.drawable.shihtzuc1)
                        shitzu4?.setBackgroundResource(R.drawable.shihtzuc5)
                        shitzu5?.setBackgroundResource(R.drawable.shihtzuc7_1)
                        shitzu6?.setBackgroundResource(R.drawable.shihtzuc6)
                        shitzu7?.setBackgroundResource(R.drawable.shihtzuc4)
                        R.drawable.shitzu3
                    }
                    "Fur6" -> {
                        shitzu1?.setBackgroundResource(R.drawable.shihtzuc2)
                        shitzu2?.setBackgroundResource(R.drawable.shihtzuc3)
                        shitzu3?.setBackgroundResource(R.drawable.shihtzuc1)
                        shitzu4?.setBackgroundResource(R.drawable.shihtzuc5)
                        shitzu5?.setBackgroundResource(R.drawable.shihtzuc7)
                        shitzu6?.setBackgroundResource(R.drawable.shihtzuc6_1)
                        shitzu7?.setBackgroundResource(R.drawable.shihtzuc4)
                        R.drawable.shitzu2
                    }
                    "Fur7" -> {
                        shitzu1?.setBackgroundResource(R.drawable.shihtzuc2)
                        shitzu2?.setBackgroundResource(R.drawable.shihtzuc3)
                        shitzu3?.setBackgroundResource(R.drawable.shihtzuc1)
                        shitzu4?.setBackgroundResource(R.drawable.shihtzuc5)
                        shitzu5?.setBackgroundResource(R.drawable.shihtzuc7)
                        shitzu6?.setBackgroundResource(R.drawable.shihtzuc6)
                        shitzu7?.setBackgroundResource(R.drawable.shihtzuc4_1)
                        R.drawable.shitzu4}
                    else -> throw IllegalArgumentException("Unknown fur color for Shitzu: $furColorName")
                }
            }
            "Chihuahua" -> {
                val ch1 = findViewById<TextView>(R.id.fur1txt)
                val ch2 = findViewById<TextView>(R.id.fur2txt)
                val ch3 = findViewById<TextView>(R.id.fur3txt)
                val ch4 = findViewById<TextView>(R.id.fur4txt)
                val ch5 = findViewById<TextView>(R.id.fur5txt)
                val ch6 = findViewById<TextView>(R.id.fur6txt)
                when (furColorName) {
                    "Fur1" -> {
                        ch1?.setBackgroundResource(R.drawable.chiuahuac3_1)
                        ch2?.setBackgroundResource(R.drawable.chiuahuac1)
                        ch3?.setBackgroundResource(R.drawable.chiuahuac2)
                        ch4?.setBackgroundResource(R.drawable.chiuahuac4)
                        ch5?.setBackgroundResource(R.drawable.chiuahuac6)
                        ch6?.setBackgroundResource(R.drawable.chiuahuac5)
                        R.drawable.chihuahua5
                    }
                    "Fur2" -> {
                        ch1?.setBackgroundResource(R.drawable.chiuahuac3)
                        ch2?.setBackgroundResource(R.drawable.chiuahuac1_1)
                        ch3?.setBackgroundResource(R.drawable.chiuahuac2)
                        ch4?.setBackgroundResource(R.drawable.chiuahuac4)
                        ch5?.setBackgroundResource(R.drawable.chiuahuac6)
                        ch6?.setBackgroundResource(R.drawable.chiuahuac5)
                        R.drawable.chihuahua1
                    }
                    "Fur3" -> {
                        ch1?.setBackgroundResource(R.drawable.chiuahuac3)
                        ch2?.setBackgroundResource(R.drawable.chiuahuac1)
                        ch3?.setBackgroundResource(R.drawable.chiuahuac2_1)
                        ch4?.setBackgroundResource(R.drawable.chiuahuac4)
                        ch5?.setBackgroundResource(R.drawable.chiuahuac6)
                        ch6?.setBackgroundResource(R.drawable.chiuahuac5)
                        R.drawable.chihuahua3
                    }
                    "Fur4" -> {
                        ch1?.setBackgroundResource(R.drawable.chiuahuac3)
                        ch2?.setBackgroundResource(R.drawable.chiuahuac1)
                        ch3?.setBackgroundResource(R.drawable.chiuahuac2)
                        ch4?.setBackgroundResource(R.drawable.chiuahuac4_1)
                        ch5?.setBackgroundResource(R.drawable.chiuahuac6)
                        ch6?.setBackgroundResource(R.drawable.chiuahuac5)
                        R.drawable.chihuahua6
                    }
                    "Fur5" -> {
                        ch1?.setBackgroundResource(R.drawable.chiuahuac3)
                        ch2?.setBackgroundResource(R.drawable.chiuahuac1)
                        ch3?.setBackgroundResource(R.drawable.chiuahuac2)
                        ch4?.setBackgroundResource(R.drawable.chiuahuac4)
                        ch5?.setBackgroundResource(R.drawable.chiuahuac6_1)
                        ch6?.setBackgroundResource(R.drawable.chiuahuac5)
                        R.drawable.chihuahua4
                    }
                    "Fur6" -> {
                        ch1?.setBackgroundResource(R.drawable.chiuahuac3)
                        ch2?.setBackgroundResource(R.drawable.chiuahuac1)
                        ch3?.setBackgroundResource(R.drawable.chiuahuac2)
                        ch4?.setBackgroundResource(R.drawable.chiuahuac4)
                        ch5?.setBackgroundResource(R.drawable.chiuahuac6)
                        ch6?.setBackgroundResource(R.drawable.chiuahuac5_1)
                        R.drawable.chihuahua2
                    }
                    else -> throw IllegalArgumentException("Unknown fur color for Shitzu: $furColorName")
                }
            }
            "Pomeranian" -> {
                val pm1 = findViewById<TextView>(R.id.fur1txt)
                val pm2 = findViewById<TextView>(R.id.fur2txt)
                val pm3 = findViewById<TextView>(R.id.fur3txt)
                val pm4 = findViewById<TextView>(R.id.fur4txt)
                val pm5 = findViewById<TextView>(R.id.fur5txt)
                val pm6 = findViewById<TextView>(R.id.fur6txt)
                when (furColorName) {
                    "Fur1" -> {
                        pm1?.setBackgroundResource(R.drawable.pomerenianc3_1)
                        pm2?.setBackgroundResource(R.drawable.pomerenianc1)
                        pm3?.setBackgroundResource(R.drawable.pomerenianc2)
                        pm4?.setBackgroundResource(R.drawable.pomerenianc4)
                        pm5?.setBackgroundResource(R.drawable.pomerenianc5)
                        pm6?.setBackgroundResource(R.drawable.pomerenianc6)
                        R.drawable.pomerenian4
                    }
                    "Fur2" -> {
                        pm1?.setBackgroundResource(R.drawable.pomerenianc3)
                        pm2?.setBackgroundResource(R.drawable.pomerenianc1_1)
                        pm3?.setBackgroundResource(R.drawable.pomerenianc2)
                        pm4?.setBackgroundResource(R.drawable.pomerenianc4)
                        pm5?.setBackgroundResource(R.drawable.pomerenianc5)
                        pm6?.setBackgroundResource(R.drawable.pomerenianc6)
                        R.drawable.pomeranian1fb
                    }
                    "Fur3" -> {
                        pm1?.setBackgroundResource(R.drawable.pomerenianc3)
                        pm2?.setBackgroundResource(R.drawable.pomerenianc1)
                        pm3?.setBackgroundResource(R.drawable.pomerenianc2_1)
                        pm4?.setBackgroundResource(R.drawable.pomerenianc4)
                        pm5?.setBackgroundResource(R.drawable.pomerenianc5)
                        pm6?.setBackgroundResource(R.drawable.pomerenianc6)
                        R.drawable.pomerenian5
                    }
                    "Fur4" -> {

                        pm1?.setBackgroundResource(R.drawable.pomerenianc3)
                        pm2?.setBackgroundResource(R.drawable.pomerenianc1)
                        pm3?.setBackgroundResource(R.drawable.pomerenianc2)
                        pm4?.setBackgroundResource(R.drawable.pomerenianc4_1)
                        pm5?.setBackgroundResource(R.drawable.pomerenianc5)
                        pm6?.setBackgroundResource(R.drawable.pomerenianc6)
                        R.drawable.pomerenian2fb
                    }
                    "Fur5" -> {
                        pm1?.setBackgroundResource(R.drawable.pomerenianc3)
                        pm2?.setBackgroundResource(R.drawable.pomerenianc1)
                        pm3?.setBackgroundResource(R.drawable.pomerenianc2)
                        pm4?.setBackgroundResource(R.drawable.pomerenianc4)
                        pm5?.setBackgroundResource(R.drawable.pomerenianc5_1)
                        pm6?.setBackgroundResource(R.drawable.pomerenianc6)
                        R.drawable.pomerenian6fb
                    }
                    "Fur6" -> {
                        pm1?.setBackgroundResource(R.drawable.pomerenianc3)
                        pm2?.setBackgroundResource(R.drawable.pomerenianc1)
                        pm3?.setBackgroundResource(R.drawable.pomerenianc2)
                        pm4?.setBackgroundResource(R.drawable.pomerenianc4)
                        pm5?.setBackgroundResource(R.drawable.pomerenianc5)
                        pm6?.setBackgroundResource(R.drawable.pomerenianc6_1)
                        R.drawable.pomerenian3
                    }
                    else -> throw IllegalArgumentException("Unknown fur color for Shitzu: $furColorName")
                }
            }
            "Poodle" -> {
                val p1 = findViewById<TextView>(R.id.fur1txt)
                val p2 = findViewById<TextView>(R.id.fur2txt)
                val p3 = findViewById<TextView>(R.id.fur3txt)
                val p4 = findViewById<TextView>(R.id.fur4txt)
                val p5 = findViewById<TextView>(R.id.fur5txt)
                val p6 = findViewById<TextView>(R.id.fur6txt)
                when (furColorName) {
                    "Fur1" -> {
                        p1?.setBackgroundResource(R.drawable.poodlec1_1)
                        p2?.setBackgroundResource(R.drawable.poodlec2)
                        p3?.setBackgroundResource(R.drawable.poodlec3)
                        p4?.setBackgroundResource(R.drawable.poodlec5)
                        p5?.setBackgroundResource(R.drawable.poodlec4)
                        p6?.setBackgroundResource(R.drawable.poodlec6)
                        R.drawable.poodle5
                    }
                    "Fur2" -> {
                        p1?.setBackgroundResource(R.drawable.poodlec1)
                        p2?.setBackgroundResource(R.drawable.poodlec2_1)
                        p3?.setBackgroundResource(R.drawable.poodlec3)
                        p4?.setBackgroundResource(R.drawable.poodlec5)
                        p5?.setBackgroundResource(R.drawable.poodlec4)
                        p6?.setBackgroundResource(R.drawable.poodlec6)
                        R.drawable.poodle4
                    }
                    "Fur3" -> {
                        p1?.setBackgroundResource(R.drawable.poodlec1)
                        p2?.setBackgroundResource(R.drawable.poodlec2)
                        p3?.setBackgroundResource(R.drawable.poodlec3_1)
                        p4?.setBackgroundResource(R.drawable.poodlec5)
                        p5?.setBackgroundResource(R.drawable.poodlec4)
                        p6?.setBackgroundResource(R.drawable.poodlec6)
                        R.drawable.poodle6
                    }
                    "Fur4" -> {
                        p1?.setBackgroundResource(R.drawable.poodlec1)
                        p2?.setBackgroundResource(R.drawable.poodlec2)
                        p3?.setBackgroundResource(R.drawable.poodlec3)
                        p4?.setBackgroundResource(R.drawable.poodlec5_1)
                        p5?.setBackgroundResource(R.drawable.poodlec4)
                        p6?.setBackgroundResource(R.drawable.poodlec6)
                        R.drawable.poodle2fb
                    }
                    "Fur5" -> {
                        p1?.setBackgroundResource(R.drawable.poodlec1)
                        p2?.setBackgroundResource(R.drawable.poodlec2)
                        p3?.setBackgroundResource(R.drawable.poodlec3)
                        p4?.setBackgroundResource(R.drawable.poodlec5)
                        p5?.setBackgroundResource(R.drawable.poodlec4_1)
                        p6?.setBackgroundResource(R.drawable.poodlec6)
                        R.drawable.poodle3
                    }
                    "Fur6" -> {
                        p1?.setBackgroundResource(R.drawable.poodlec1)
                        p2?.setBackgroundResource(R.drawable.poodlec2)
                        p3?.setBackgroundResource(R.drawable.poodlec3)
                        p4?.setBackgroundResource(R.drawable.poodlec5)
                        p5?.setBackgroundResource(R.drawable.poodlec4)
                        p6?.setBackgroundResource(R.drawable.poodlec6_1)
                        R.drawable.poodle1fb
                    }
                    else -> throw IllegalArgumentException("Unknown fur color for Shitzu: $furColorName")
                }
            }
            else -> throw IllegalArgumentException("Please select breed first!")
        }
    }
    private fun updateBreedFurColorView(breed: String, furColorName: String) {
        val breedView: View? = when (breed) {
            "Golden Retriever" -> findViewById(R.id.gretriever)
            "Shih Tzu" -> findViewById(R.id.shitzu)
            "Chihuahua" -> findViewById(R.id.chihuahua)
            "Pomeranian" -> findViewById(R.id.pomeranian)
            "Poodle" -> findViewById(R.id.poodle)
            else -> null
        }

        breedView?.let {
            val drawableResourceId = getDrawableResourceId(breed, furColorName)
            it.setBackgroundResource(drawableResourceId)
        }
    }

    private val breedFurAccessoryMap = mapOf(
        R.id.colar1fragment to "Collar1",
        R.id.colar2 to "Collar2",
        R.id.scarf1 to "Scarf1",
        R.id.scarf2 to "Scarf2",
        R.id.ribbon1 to "Ribbon1",
        R.id.ribbon2 to "Ribbon2"
    )
    var selectedAccessory: String? = null

    override fun onAccessorySelected(accessoryId: Int) {
        val selectedAccessoryValue = breedFurAccessoryMap[accessoryId]
        selectedAccessoryValue?.let { accessory ->
            if (accessory == selectedAccessory) {
                // Deselect the accessory if it's already selected
                selectedAccessory = null
            } else {
                // Select the new accessory
                selectedAccessory = accessory
            }
            updateBreedFurColorAccessoryView(selectedBreed, selectedFurColor, selectedAccessory)
        }
    }

    private fun getDrawableResourceId(breed: String, furColorName: String, accessory: String?): Int {
        // Return the resource ID of the drawable based on the breed, fur color name, and accessory
        return when (breed) {
            "Golden Retriever" -> {
                when (furColorName) {
                    "Fur1" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.golden1_collar1
                            "Collar2" -> R.drawable.golden1_collar2
                            "Scarf1" -> R.drawable.golden1_scarf1
                            "Scarf2" -> R.drawable.golden1_scarf2
                            "Ribbon1" -> R.drawable.golden1_ribbon1
                            "Ribbon2" -> R.drawable.golden1_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur2" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.golden2_collar1
                            "Collar2" -> R.drawable.golden2_collar2
                            "Scarf1" -> R.drawable.golden2_scarf1
                            "Scarf2" -> R.drawable.golden2_scarf2
                            "Ribbon1" -> R.drawable.golden2_ribbon1
                            "Ribbon2" -> R.drawable.golden2_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur3" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.golden3_collar1
                            "Collar2" -> R.drawable.golden3_collar2
                            "Scarf1" -> R.drawable.golden3_scarf1
                            "Scarf2" -> R.drawable.golden3_scarf2
                            "Ribbon1" -> R.drawable.golden3_ribbon1
                            "Ribbon2" -> R.drawable.golden3_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur4" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.golden4_collar1
                            "Collar2" -> R.drawable.golden4_collar2
                            "Scarf1" -> R.drawable.golden4_scarf1
                            "Scarf2" -> R.drawable.golden4_scarf2
                            "Ribbon1" -> R.drawable.golden4_ribbon1
                            "Ribbon2" -> R.drawable.golden4_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur5" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.golden5_collar1
                            "Collar2" -> R.drawable.golden5_collar2
                            "Scarf1" -> R.drawable.golden5_scarf1
                            "Scarf2" -> R.drawable.golden5_scarf2
                            "Ribbon1" -> R.drawable.golden5_ribbon1
                            "Ribbon2" -> R.drawable.golden5_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur6" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.golden6_collar1
                            "Collar2" -> R.drawable.golden6_collar2
                            "Scarf1" -> R.drawable.golden6_scarf1
                            "Scarf2" -> R.drawable.golden6_scarf2
                            "Ribbon1" -> R.drawable.golden6_ribbon1
                            "Ribbon2" -> R.drawable.golden6_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    else -> throw IllegalArgumentException("Unknown fur color for Golden Retriever: $furColorName")
                }
            }
            "Shih Tzu" -> {
                when (furColorName) {
                    "Fur1" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.shitzu7_collar1
                            "Collar2" -> R.drawable.shitzu7_collar2
                            "Scarf1" -> R.drawable.shitzu7_scarf1
                            "Scarf2" -> R.drawable.shitzu7_scarf2
                            "Ribbon1" -> R.drawable.shitzu7_ribbon1
                            "Ribbon2" -> R.drawable.shitzu7_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur2" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.shitzu5_collar1
                            "Collar2" -> R.drawable.shitzu5_collar2
                            "Scarf1" -> R.drawable.shitzu5_scarf1
                            "Scarf2" -> R.drawable.shitzu5_scarf2
                            "Ribbon1" -> R.drawable.shitzu5_ribbon1
                            "Ribbon2" -> R.drawable.shitzu5_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur3" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.shitzu6_collar1
                            "Collar2" -> R.drawable.shitzu6_collar2
                            "Scarf1" -> R.drawable.shitzu6_scarf1
                            "Scarf2" -> R.drawable.shitzu6_scarf2
                            "Ribbon1" -> R.drawable.shitzu6_ribbon1
                            "Ribbon2" -> R.drawable.shitzu6_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur4" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.shitzu1_collar1
                            "Collar2" -> R.drawable.shitzu1_collar2
                            "Scarf1" -> R.drawable.shitzu1_scarf1
                            "Scarf2" -> R.drawable.shitzu1_scarf2
                            "Ribbon1" -> R.drawable.shitzu1_ribbon1
                            "Ribbon2" -> R.drawable.shitzu1_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur5" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.shitzu3_collar1
                            "Collar2" -> R.drawable.shitzu3_collar2
                            "Scarf1" -> R.drawable.shitzu3_scarf1
                            "Scarf2" -> R.drawable.shitzu3_scarf2
                            "Ribbon1" -> R.drawable.shitzu3_ribbon1
                            "Ribbon2" -> R.drawable.shitzu3_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur6" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.shitzu2_collar1
                            "Collar2" -> R.drawable.shitzu2_collar2
                            "Scarf1" -> R.drawable.shitzu2_scarf1
                            "Scarf2" -> R.drawable.shitzu2_scarf2
                            "Ribbon1" -> R.drawable.shitzu2_ribbon1
                            "Ribbon2" -> R.drawable.shitzu2_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur7" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.shitzu4_collar1
                            "Collar2" -> R.drawable.shitzu4_collar2
                            "Scarf1" -> R.drawable.shitzu4_scarf1
                            "Scarf2" -> R.drawable.shitzu4_scarf2
                            "Ribbon1" -> R.drawable.shitzu4_ribbon1
                            "Ribbon2" -> R.drawable.shitzu4_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    else -> throw IllegalArgumentException("Unknown fur color for Shitzu: $furColorName")
                }
            }
            "Chihuahua" -> {
                when (furColorName) {
                    "Fur1" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.chihuahua5_collar1
                            "Collar2" -> R.drawable.chihuahua5_collar2
                            "Scarf1" -> R.drawable.chihuahua5_scarf1
                            "Scarf2" -> R.drawable.chihuahua5_scarf2
                            "Ribbon1" -> R.drawable.chihuahua5_ribbon1
                            "Ribbon2" -> R.drawable.chihuahua5_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur2" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.chihuahua1_collar1
                            "Collar2" -> R.drawable.chihuahua1_collar2
                            "Scarf1" -> R.drawable.chihuahua1_scarf1
                            "Scarf2" -> R.drawable.chihuahua1_scarf2
                            "Ribbon1" -> R.drawable.chihuahua1_ribbon1
                            "Ribbon2" -> R.drawable.chihuahua1_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur3" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.chihuahua3_collar1
                            "Collar2" -> R.drawable.chihuahua3_collar2
                            "Scarf1" -> R.drawable.chihuahua3_scarf1
                            "Scarf2" -> R.drawable.chihuahua3_scarf2
                            "Ribbon1" -> R.drawable.chihuahua3_ribbon1
                            "Ribbon2" -> R.drawable.chihuahua3_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur4" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.chihuahua7_collar1
                            "Collar2" -> R.drawable.chihuahua7_collar2
                            "Scarf1" -> R.drawable.chihuahua7_scarf1
                            "Scarf2" -> R.drawable.chihuahua7_scarf2
                            "Ribbon1" -> R.drawable.chihuahua7_ribbon1
                            "Ribbon2" -> R.drawable.chihuahua7_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur5" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.chihuahua4_collar1
                            "Collar2" -> R.drawable.chihuahua4_collar2
                            "Scarf1" -> R.drawable.chihuahua4_scarf1
                            "Scarf2" -> R.drawable.chihuahua4_scarf2
                            "Ribbon1" -> R.drawable.chihuahua4_ribbon1
                            "Ribbon2" -> R.drawable.chihuahua4_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur6" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.chihuahua2_collar1
                            "Collar2" -> R.drawable.chihuahua2_collar2
                            "Scarf1" -> R.drawable.chihuahua2_scarf1
                            "Scarf2" -> R.drawable.chihuahua2_scarf2
                            "Ribbon1" -> R.drawable.chihuahua2_ribbon1
                            "Ribbon2" -> R.drawable.chihuahua2_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    else -> throw IllegalArgumentException("Unknown fur color for Shitzu: $furColorName")
                }
            }
            "Pomeranian" -> {
                when (furColorName) {
                    "Fur1" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.pomerenian4_collar1
                            "Collar2" -> R.drawable.pomerenian4_collar2
                            "Scarf1" -> R.drawable.pomerenian4_scarf1
                            "Scarf2" -> R.drawable.pomerenian4_scarf2
                            "Ribbon1" -> R.drawable.pomerenian4_ribbon1
                            "Ribbon2" -> R.drawable.pomerenian4_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur2" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.pomerenian1_collar1
                            "Collar2" -> R.drawable.pomerenian1_collar2
                            "Scarf1" -> R.drawable.pomerenian1_scarf1
                            "Scarf2" -> R.drawable.pomerenian1_scarf2
                            "Ribbon1" -> R.drawable.pomerenian1_ribbon1
                            "Ribbon2" -> R.drawable.pomerenian1_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur3" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.pomerenian5_collar1
                            "Collar2" -> R.drawable.pomerenian5_collar2
                            "Scarf1" -> R.drawable.pomerenian5_scarf1
                            "Scarf2" -> R.drawable.pomerenian5_scarf2
                            "Ribbon1" -> R.drawable.pomerenian5_ribbon1
                            "Ribbon2" -> R.drawable.pomerenian5_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur4" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.pomerenian2_collar1
                            "Collar2" -> R.drawable.pomerenian2_collar2
                            "Scarf1" -> R.drawable.pomerenian2_scarf1
                            "Scarf2" -> R.drawable.pomerenian2_scarf2
                            "Ribbon1" -> R.drawable.pomerenian2_ribbon1
                            "Ribbon2" -> R.drawable.pomerenian2_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur5" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.pomerenian6_collar1
                            "Collar2" -> R.drawable.pomerenian6_collar2
                            "Scarf1" -> R.drawable.pomerenian6_scarf1
                            "Scarf2" -> R.drawable.pomerenian6_scarf2
                            "Ribbon1" -> R.drawable.pomerenian6_ribbon1
                            "Ribbon2" -> R.drawable.pomerenian6_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur6" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.pomerenian3_collar1
                            "Collar2" -> R.drawable.pomerenian3_collar2
                            "Scarf1" -> R.drawable.pomerenian3_scarf1
                            "Scarf2" -> R.drawable.pomerenian3_scarf2
                            "Ribbon1" -> R.drawable.pomerenian3_ribbon1
                            "Ribbon2" -> R.drawable.pomerenian3_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    else -> throw IllegalArgumentException("Unknown fur color for Shitzu: $furColorName")
                }
            }
            "Poodle" -> {
                when (furColorName) {
                    "Fur1" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.poodle5_collar1
                            "Collar2" -> R.drawable.poodle5_collar2
                            "Scarf1" -> R.drawable.poodle5_scarf1
                            "Scarf2" -> R.drawable.poodle5_scarf2
                            "Ribbon1" -> R.drawable.poodle5_ribbon1
                            "Ribbon2" -> R.drawable.poodle5_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur2" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.poodle4_collar1
                            "Collar2" -> R.drawable.poodle4_collar2
                            "Scarf1" -> R.drawable.poodle4_scarf1
                            "Scarf2" -> R.drawable.poodle4_scarf2
                            "Ribbon1" -> R.drawable.poodle4_ribbon1
                            "Ribbon2" -> R.drawable.poodle4_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur3" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.poodle6_collar1
                            "Collar2" -> R.drawable.poodle6_collar2
                            "Scarf1" -> R.drawable.poodle6_scarf1
                            "Scarf2" -> R.drawable.poodle6_scarf2
                            "Ribbon1" -> R.drawable.poodle6_ribbon1
                            "Ribbon2" -> R.drawable.poodle6_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur4" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.poodle2_collar1
                            "Collar2" -> R.drawable.poodle2_collar2
                            "Scarf1" -> R.drawable.poodle2_scarf1
                            "Scarf2" -> R.drawable.poodle2_scarf2
                            "Ribbon1" -> R.drawable.poodle2_ribbon1
                            "Ribbon2" -> R.drawable.poodle2_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur5" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.poodle3_collar1
                            "Collar2" -> R.drawable.poodle3_collar2
                            "Scarf1" -> R.drawable.poodle3_scarf1
                            "Scarf2" -> R.drawable.poodle3_scarf2
                            "Ribbon1" -> R.drawable.poodle3_ribbon1
                            "Ribbon2" -> R.drawable.poodle3_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    "Fur6" -> {
                        when (accessory) {
                            "Collar1" -> R.drawable.poodle1_collar1
                            "Collar2" -> R.drawable.poodle1_collar2
                            "Scarf1" -> R.drawable.poodle1_scarf1
                            "Scarf2" -> R.drawable.poodle1_scarf2
                            "Ribbon1" -> R.drawable.poodle1_ribbon1
                            "Ribbon2" -> R.drawable.poodle1_ribbon2
                            else -> throw IllegalArgumentException("Unknown fur accessory for Golden Retriever: $accessory")
                        }
                    }
                    else -> throw IllegalArgumentException("Unknown fur color for Shitzu: $furColorName")
                }
            }
            else -> throw IllegalArgumentException("Unknown breed: $breed")
        }
    }

    private fun updateBreedFurColorAccessoryView(breed: String, furColorName: String, accessory: String?) {
        val breedView: View? = when (breed) {
            "Golden Retriever" -> findViewById(R.id.gretriever)
            "Shih Tzu" -> findViewById(R.id.shitzu)
            "Chihuahua" -> findViewById(R.id.chihuahua)
            "Pomeranian" -> findViewById(R.id.pomeranian)
            "Poodle" -> findViewById(R.id.poodle)
            else -> null
        }

        breedView?.let {
            val drawableResourceId = when (accessory) {
                null -> {
                    // If no accessory is selected, returns to fur color
                    getDrawableResourceId(breed, furColorName)
                }
                else -> {
                    // If an accessory is selected, remains the accesory drawable
                    getDrawableResourceId(breed, furColorName, accessory)
                }
            }
            it.setBackgroundResource(drawableResourceId)
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Create a custom function to show a confirmation dialog for exiting the application
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Skip Dog Avatar Customization?")
        alertDialogBuilder.setMessage("Are you sure you want to skip adding an avatar for your dog? You won't be able to return to your progress and your dog avatar will not be visible but you can edit your dog avatar later. \n\nYou will proceed to Homepage if you confirm.")
        alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
            val intent = Intent(this, homepageold::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            // If user cancels, dismiss the dialog (do nothing)
            dialog.dismiss()
        }

        // Create and show the dialog
        val exitConfirmationDialog = alertDialogBuilder.create()
        exitConfirmationDialog.show()
    }


}




