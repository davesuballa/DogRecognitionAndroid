package com.example.buddy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SecondFragment : Fragment() {

    private lateinit var furColorSelectedListener: OnFurColorSelectedListener
    private lateinit var sharedViewModel: SharedViewModel

    interface OnFurColorSelectedListener {
        fun onFurColorSelected(furColorId: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Observe changes in breed in SecondFragment using method reference
        sharedViewModel.getBreed().observe(viewLifecycleOwner, this::updateButtonsForBreed)

        // Assuming you have buttons for fur colors with IDs furColor1Button, furColor2Button, etc.
        view.findViewById<Button>(R.id.fur1btn).setOnClickListener {
            handleButtonClick(R.id.fur1btn)
            sharedViewModel.setfurColor("Fur1")
        }

        view.findViewById<Button>(R.id.fur2btn).setOnClickListener {
            handleButtonClick(R.id.fur2btn)
            sharedViewModel.setfurColor("Fur2")
        }

        view.findViewById<Button>(R.id.fur3btn).setOnClickListener {
            handleButtonClick(R.id.fur3btn)
            sharedViewModel.setfurColor("Fur3")
        }

        view.findViewById<Button>(R.id.fur4btn).setOnClickListener {
            handleButtonClick(R.id.fur4btn)
            sharedViewModel.setfurColor("Fur4")
        }

        view.findViewById<Button>(R.id.fur5btn).setOnClickListener {
            handleButtonClick(R.id.fur5btn)
            sharedViewModel.setfurColor("Fur5")
        }

        view.findViewById<Button>(R.id.fur6btn).setOnClickListener {
            handleButtonClick(R.id.fur6btn)
            sharedViewModel.setfurColor("Fur6")
        }

        view.findViewById<Button>(R.id.fur7btn).setOnClickListener {
            handleButtonClick(R.id.fur7btn)
            sharedViewModel.setfurColor("Fur7")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        furColorSelectedListener = if (context is OnFurColorSelectedListener) {
            context
        } else {
            throw RuntimeException("$context must implement OnFurColorSelectedListener")
        }
    }

    fun updateButtonsForBreed(breed: String) {
        // Update the visibility of buttons based on the selected breed and background drawable
        when (breed) {
            "Golden Retriever" -> {
                view?.findViewById<TextView>(R.id.fur1txt)?.setBackgroundResource(R.drawable.goldenc1)
                view?.findViewById<TextView>(R.id.fur2txt)?.setBackgroundResource(R.drawable.goldenc2)
                view?.findViewById<TextView>(R.id.fur3txt)?.setBackgroundResource(R.drawable.goldenc3)
                view?.findViewById<TextView>(R.id.fur4txt)?.setBackgroundResource(R.drawable.goldenc4)
                view?.findViewById<TextView>(R.id.fur5txt)?.setBackgroundResource(R.drawable.goldenc5)
                view?.findViewById<TextView>(R.id.fur6txt)?.setBackgroundResource(R.drawable.goldenc6)
                view?.findViewById<Button>(R.id.fur7btn)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.fur7txt)?.visibility = View.GONE



            }

            "Shih Tzu" ->{
                view?.findViewById<TextView>(R.id.fur1txt)?.setBackgroundResource(R.drawable.shihtzuc2)
                view?.findViewById<TextView>(R.id.fur2txt)?.setBackgroundResource(R.drawable.shihtzuc3)
                view?.findViewById<TextView>(R.id.fur3txt)?.setBackgroundResource(R.drawable.shihtzuc1)
                view?.findViewById<TextView>(R.id.fur4txt)?.setBackgroundResource(R.drawable.shihtzuc5)
                view?.findViewById<TextView>(R.id.fur5txt)?.setBackgroundResource(R.drawable.shihtzuc7)
                view?.findViewById<TextView>(R.id.fur6txt)?.setBackgroundResource(R.drawable.shihtzuc6)
                view?.findViewById<Button>(R.id.fur7btn)?.visibility = View.VISIBLE
                view?.findViewById<TextView>(R.id.fur7txt)?.visibility = View.VISIBLE
            }

            "Chihuahua" -> {
                view?.findViewById<TextView>(R.id.fur1txt)?.setBackgroundResource(R.drawable.chiuahuac3)
                view?.findViewById<TextView>(R.id.fur2txt)?.setBackgroundResource(R.drawable.chiuahuac1)
                view?.findViewById<TextView>(R.id.fur3txt)?.setBackgroundResource(R.drawable.chiuahuac2)
                view?.findViewById<TextView>(R.id.fur4txt)?.setBackgroundResource(R.drawable.chiuahuac4)
                view?.findViewById<TextView>(R.id.fur5txt)?.setBackgroundResource(R.drawable.chiuahuac6)
                view?.findViewById<TextView>(R.id.fur6txt)?.setBackgroundResource(R.drawable.chiuahuac5)
                view?.findViewById<Button>(R.id.fur7btn)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.fur7txt)?.visibility = View.GONE

            }

            "Pomeranian" -> {
                view?.findViewById<TextView>(R.id.fur1txt)?.setBackgroundResource(R.drawable.pomerenianc3)
                view?.findViewById<TextView>(R.id.fur2txt)?.setBackgroundResource(R.drawable.pomerenianc1)
                view?.findViewById<TextView>(R.id.fur3txt)?.setBackgroundResource(R.drawable.pomerenianc2)
                view?.findViewById<TextView>(R.id.fur4txt)?.setBackgroundResource(R.drawable.pomerenianc4)
                view?.findViewById<TextView>(R.id.fur5txt)?.setBackgroundResource(R.drawable.pomerenianc5)
                view?.findViewById<TextView>(R.id.fur6txt)?.setBackgroundResource(R.drawable.pomerenianc6)
                view?.findViewById<Button>(R.id.fur7btn)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.fur7txt)?.visibility = View.GONE
            }

            "Poodle" -> {
                view?.findViewById<TextView>(R.id.fur1txt)?.setBackgroundResource(R.drawable.poodlec1)
                view?.findViewById<TextView>(R.id.fur2txt)?.setBackgroundResource(R.drawable.poodlec2)
                view?.findViewById<TextView>(R.id.fur3txt)?.setBackgroundResource(R.drawable.poodlec3)
                view?.findViewById<TextView>(R.id.fur4txt)?.setBackgroundResource(R.drawable.poodlec5)
                view?.findViewById<TextView>(R.id.fur5txt)?.setBackgroundResource(R.drawable.poodlec4)
                view?.findViewById<TextView>(R.id.fur6txt)?.setBackgroundResource(R.drawable.poodlec6)
                view?.findViewById<Button>(R.id.fur7btn)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.fur7txt)?.visibility = View.GONE
            }
            else -> {
                // Null
            }
        }
    }

    private fun handleButtonClick(furColorId: Int) {
        // Check if breed is selected
        val breed = sharedViewModel.getBreed().value
        if (breed.isNullOrBlank()) {
            // Breed is not selected, show a toast message
            showToast("Please select a breed first!")
        } else {
            // Breed is selected, notify the listener
            furColorSelectedListener.onFurColorSelected(furColorId)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(): SecondFragment {
            return SecondFragment()
        }
    }

}
