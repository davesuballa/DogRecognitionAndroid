package com.example.buddy

// ProfileAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.content.Intent
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ProfileAdapter(
    private val userDatabase: DatabaseReference,
    private val auth: FirebaseAuth
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_PROFILE = 0
        private const val VIEW_TYPE_ADD_DOG = 1
    }

    private var profileList: List<String> = emptyList()
    private var onItemClickListener: ((String) -> Unit)? = null


    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ViewHolder for displaying dog profile
        val profileNameTextView: TextView = itemView.findViewById(R.id.name1)
        val profiledogicon : TextView = itemView.findViewById(R.id.profileicon)

        init {
            itemView.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val dogId = profileList[position]
                    onItemClickListener?.invoke(dogId)
                }
            }
        }

    }

    inner class AddDogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ViewHolder for displaying "Add Dog" button
        val addProfileButton: Button = itemView.findViewById(R.id.add_dog_profile)

        init {
            addProfileButton.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, add_new_dog::class.java)

                // Create custom animation options for starting the new activity
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    context,
                    R.anim.slide_up,   // Animation for the new activity (slide up)
                    R.anim.slide_down   // Animation for the current activity (slide down)
                )

                // Start the activity with the specified animation options
                context.startActivity(intent, options.toBundle())
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PROFILE -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_profile, parent, false)
                ProfileViewHolder(itemView)
            }
            VIEW_TYPE_ADD_DOG -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_add_dog, parent, false) // Create layout for "Add Dog" button
                AddDogViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileViewHolder -> {
                val userCurrentID = auth.currentUser?.uid
                if (userCurrentID != null && position < profileList.size) {
                    val dogId = profileList[position]
                    val userDogsRef = userDatabase.child(userCurrentID).child("Dogs").child(dogId)
                    userDogsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val breed = snapshot.child("breedName").getValue(String::class.java) ?: ""
                                val fur = snapshot.child("furColor").getValue(String::class.java) ?: ""
                                val accessory = snapshot.child("accessory").getValue(String::class.java) ?: ""

                                val dogName = snapshot.child("dogName").getValue(String::class.java) ?: ""
                                holder.profileNameTextView.text = dogName

                                if (breed.isNotBlank() && fur.isNotBlank() && accessory.isNotBlank()) {
                                    // All necessary values are present, show the profile icon
                                    val resourceId = getDrawableResourceId(breed, fur, accessory)
                                    holder.profiledogicon.setBackgroundResource(resourceId)
                                } else if (breed.isNotBlank() && fur.isNotBlank() && accessory.isBlank()){
                                    val resourceId = getDrawableResourceId(breed, fur, accessory)
                                    holder.profiledogicon.setBackgroundResource(resourceId)
                                }

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                        }
                    })
                }
            }
            is AddDogViewHolder -> {
                // Handle click listener for the "Add Dog" button
                holder.addProfileButton.setOnClickListener {
                    val context = holder.itemView.context
                    val intent = Intent(context, add_new_dog::class.java)

                    // Create custom animation options for starting the new activity
                    val options = ActivityOptionsCompat.makeCustomAnimation(
                        context,
                        R.anim.slide_up,   // Animation for the new activity (slide up)
                        R.anim.fade_out   // Animation for the current activity (slide down)
                    )

                    // Start the activity with the specified animation options
                    context.startActivity(intent, options.toBundle())
                }
            }
        }
    }


    override fun getItemCount(): Int {
        // Return the size of the profile list plus one for the "Add Dog" button
        return profileList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        // Return the appropriate view type based on the position
        return if (position < profileList.size) {
            VIEW_TYPE_PROFILE
        } else {
            VIEW_TYPE_ADD_DOG
        }
    }

    // Method to update the profile list
    fun updateProfileList(newProfileList: List<String>) {
        profileList = newProfileList
        notifyDataSetChanged()
    }

    // Private method to determine the drawable resource ID based on breed, fur, and accessory
    private fun getDrawableResourceId(breed: String, fur: String, accessory: String): Int {
        // Return the resource ID of the drawable based on the breed, fur, and accessory
        return when {
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "" -> R.drawable.golden1_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.golden1_collar1_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.golden1_collar2_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.golden1_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.golden1_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.golden1_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.golden1_ribbon2_icon

            breed == "Golden Retriever" && fur == "Fur2" && accessory == "" -> R.drawable.golden2_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.golden2_collar1_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.golden2_collar2_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.golden2_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.golden2_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.golden2_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.golden2_ribbon2_icon

            breed == "Golden Retriever" && fur == "Fur3" && accessory == "" -> R.drawable.golden3_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.golden3_collar1_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.golden3_collar2_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.golden3_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.golden3_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.golden3_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.golden3_ribbon2_icon

            breed == "Golden Retriever" && fur == "Fur4" && accessory == "" -> R.drawable.golden4_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.golden4_collar1_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.golden4_collar2_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.golden4_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.golden4_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.golden4_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.golden4_ribbon2_icon

            breed == "Golden Retriever" && fur == "Fur5" && accessory == "" -> R.drawable.golden5_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.golden5_collar1_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.golden5_collar2_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.golden5_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.golden5_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.golden5_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.golden5_ribbon2_icon

            breed == "Golden Retriever" && fur == "Fur6" && accessory == "" -> R.drawable.golden6_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.golden6_collar1_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.golden6_collar2_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.golden6_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.golden6_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.golden6_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.golden6_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur1" && accessory == "" -> R.drawable.shitzu7_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.shitzu7_collar1_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.shitzu7_collar2_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.shitzu7_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.shitzu7_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.shitzu7_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.shitzu7_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur2" && accessory == "" -> R.drawable.shitzu5_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.shitzu5_collar1_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.shitzu5_collar2_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.shitzu5_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.shitzu5_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.shitzu5_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.shitzu5_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur3" && accessory == "" -> R.drawable.shitzu6_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.shitzu6_collar1_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.shitzu6_collar2_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.shitzu6_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.shitzu6_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.shitzu6_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.shitzu6_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur4" && accessory == "" -> R.drawable.shitzu1_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.shitzu1_collar1_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.shitzu1_collar2_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.shitzu1_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.shitzu1_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.shitzu1_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.shitzu1_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur5" && accessory == "" -> R.drawable.shitzu3_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.shitzu3_collar1_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.shitzu3_collar2_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.shitzu3_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.shitzu3_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.shitzu3_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.shitzu3_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur6" && accessory == "" -> R.drawable.shitzu2_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.shitzu2_collar1_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.shitzu2_collar2_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.shitzu2_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.shitzu2_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.shitzu2_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.shitzu2_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur7" && accessory == "" -> R.drawable.shitzu4_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Collar1" -> R.drawable.shitzu4_collar1_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Collar2" -> R.drawable.shitzu4_collar2_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Scarf1" -> R.drawable.shitzu4_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Scarf2" -> R.drawable.shitzu4_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Ribbon1" -> R.drawable.shitzu4_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Ribbon2" -> R.drawable.shitzu4_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur1" && accessory == "" -> R.drawable.chihuahua5_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.chihuahua5_collar1_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.chihuahua5_collar2_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.chihuahua5_scarf1_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.chihuahua5_scarf2_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.chihuahua5_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.chihuahua5_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur2" && accessory == "" -> R.drawable.chihuahua1_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.chihuahua1_collar1_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.chihuahua1_collar2_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.chihuahua1_scarf1_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.chihuahua1_scarf2_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.chihuahua1_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.chihuahua1_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur3" && accessory == "" -> R.drawable.chihuahua3_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.chihuahua3_collar1_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.chihuahua3_collar2_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.chihuahua3_scarf1_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.chihuahua3_scarf2_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.chihuahua3_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.chihuahua3_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur4" && accessory == "" -> R.drawable.chihuahua7_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.chihuahua7_collar1_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.chihuahua7_collar2_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.chihuahua7_scarf1_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.chihuahua7_scarf2_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.chihuahua7_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.chihuahua7_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur5" && accessory == "" -> R.drawable.chihuahua4_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.chihuahua4_collar1_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.chihuahua4_collar2_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.chihuahua4_scarf1_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.chihuahua4_scarf2_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.chihuahua4_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.chihuahua4_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur6" && accessory == "" -> R.drawable.chihuahua2_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.chihuahua2_collar1_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.chihuahua2_collar2_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.chihuahua2_scarf1_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.chihuahua2_scarf2_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.chihuahua2_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.chihuahua2_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur1" && accessory == "" -> R.drawable.pomerenian4_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.pomerenian4_collar1_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.pomerenian4_collar2_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.pomerenian4_scarf1_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.pomerenian4_scarf2_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.pomerenian4_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.pomerenian4_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur2" && accessory == "" -> R.drawable.pomerenian1_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.pomerenian1_collar1_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.pomerenian1_collar2_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.pomerenian1_scarf1_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.pomerenian1_scarf2_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.pomerenian1_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.pomerenian1_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur3" && accessory == "" -> R.drawable.pomerenian5_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.pomerenian5_collar1_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.pomerenian5_collar2_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.pomerenian5_scarf1_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.pomerenian5_scarf2_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.pomerenian5_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.pomerenian5_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur4" && accessory == "" -> R.drawable.pomerenian2_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.pomerenian2_collar1_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.pomerenian2_collar2_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.pomerenian2_scarf1_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.pomerenian2_scarf2_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.pomerenian2_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.pomerenian2_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur5" && accessory == "" -> R.drawable.pomerenian6_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.pomerenian6_collar1_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.pomerenian6_collar2_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.pomerenian6_scarf1_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.pomerenian6_scarf2_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.pomerenian6_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.pomerenian6_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur6" && accessory == "" -> R.drawable.pomerenian3_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.pomerenian3_collar1_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.pomerenian3_collar2_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.pomerenian3_scarf1_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.pomerenian3_scarf2_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.pomerenian3_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.pomerenian3_ribbon2_icon

            breed == "Poodle" && fur == "Fur1" && accessory == "" -> R.drawable.poodle5_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.poodle5_collar1_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.poodle5_collar2_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.poodle5_scarf1_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.poodle5_scarf2_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.poodle5_ribbon1_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.poodle5_ribbon2_icon

            breed == "Poodle" && fur == "Fur2" && accessory == "" -> R.drawable.poodle4_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.poodle4_collar1_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.poodle4_collar2_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.poodle4_scarf1_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.poodle4_scarf2_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.poodle4_ribbon1_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.poodle4_ribbon2_icon

            breed == "Poodle" && fur == "Fur3" && accessory == "" -> R.drawable.poodle6_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.poodle6_collar1_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.poodle6_collar2_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.poodle6_scarf1_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.poodle6_scarf2_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.poodle6_ribbon1_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.poodle6_ribbon2_icon

            breed == "Poodle" && fur == "Fur4" && accessory == "" -> R.drawable.poodle2_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.poodle2_collar1_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.poodle2_collar2_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.poodle2_scarf1_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.poodle2_scarf2_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.poodle2_ribbon1_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.poodle2_ribbon2_icon

            breed == "Poodle" && fur == "Fur5" && accessory == "" -> R.drawable.poodle3_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.poodle3_collar1_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.poodle3_collar2_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.poodle3_scarf1_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.poodle3_scarf2_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.poodle3_ribbon1_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.poodle3_ribbon2_icon

            breed == "Poodle" && fur == "Fur6" && accessory == "" -> R.drawable.poodle1_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.poodle1_collar1_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.poodle1_collar2_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.poodle1_scarf1_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.poodle1_scarf2_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.poodle1_ribbon1_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.poodle1_ribbon2_icon

            // Add more conditions for other combinations
            else -> R.drawable.profile // Default image in case no specific combination matches
        }
    }
}
