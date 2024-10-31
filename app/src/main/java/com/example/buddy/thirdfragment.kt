package com.example.buddy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class thirdfragment : Fragment() {


    private lateinit var accessorySelectedListener: OnAccessorySelectedListener
    private lateinit var sharedViewModel: SharedViewModel
    private val accessoryDrawableMap = mutableMapOf<Int, Pair<Int, Boolean>>(
        R.id.colar1fragment to Pair(R.drawable.acc6, false),
        R.id.colar2 to Pair(R.drawable.acc5, false),
        R.id.scarf1 to Pair(R.drawable.acc2, false),
        R.id.scarf2 to Pair(R.drawable.acc1, false),
        R.id.ribbon1 to Pair(R.drawable.acc4, false),
        R.id.ribbon2 to Pair(R.drawable.acc3, false)
    )
    private val clickedAccessories = mutableSetOf<Int>() // Set to track clicked accessory IDs
    interface OnAccessorySelectedListener {
        fun onAccessorySelected(accessoryId: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_thirdfragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        accessorySelectedListener = requireActivity() as OnAccessorySelectedListener

        view.findViewById<Button>(R.id.colar1fragment).setOnClickListener {
            handleButtonClick(R.id.colar1fragment)
        }

        view.findViewById<Button>(R.id.colar2).setOnClickListener {
            handleButtonClick(R.id.colar2)
        }

        view.findViewById<Button>(R.id.scarf1).setOnClickListener {
            handleButtonClick(R.id.scarf1)
        }

        view.findViewById<Button>(R.id.scarf2).setOnClickListener {
            handleButtonClick(R.id.scarf2)
        }

        view.findViewById<Button>(R.id.ribbon1).setOnClickListener {
            handleButtonClick(R.id.ribbon1)
        }

        view.findViewById<Button>(R.id.ribbon2).setOnClickListener {
            handleButtonClick(R.id.ribbon2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        accessorySelectedListener = if (context is OnAccessorySelectedListener) {
            context
        } else {
            throw RuntimeException("$context must implement OnAccessorySelectedListener")
        }
    }

    private fun handleButtonClick(accessoryId: Int) {
        val breed = sharedViewModel.getBreed().value
        val furColor = sharedViewModel.getFur().value

        if (breed.isNullOrBlank()) {
            showToast("Please select a breed first!")
        } else if (furColor.isNullOrBlank()) {
            showToast("Please select a fur color first!")
        } else if (breed.isNotBlank() && furColor.isNullOrBlank()) {
            showToast("Breed selected, but fur color is missing. Please select a fur color!")
        } else {
            accessorySelectedListener.onAccessorySelected(accessoryId)
            // Apply background changes here as before...
            applyBackgroundChanges(accessoryId)
        }
    }

    private fun applyBackgroundChanges(accessoryId: Int) {
        val view = requireView()

        // Reset all accessories to their default drawables except the one that was clicked
        resetAccessories(view, accessoryId)

        // Toggle the drawable of the clicked accessory
        val accessoryData = accessoryDrawableMap[accessoryId]
        if (accessoryData != null) {
            val currentDrawable = accessoryData.first
            val toggled = !accessoryData.second
            val newDrawable = if (toggled) getToggledDrawable(currentDrawable) else currentDrawable
            view.findViewById<TextView>(getAccessoryTextViewId(accessoryId)).setBackgroundResource(newDrawable)
            accessoryDrawableMap[accessoryId] = Pair(currentDrawable, toggled)
        }
    }

    private fun resetAccessories(view: View, clickedAccessoryId: Int) {
        accessoryDrawableMap.forEach { (accessoryId, drawableState) ->
            if (accessoryId != clickedAccessoryId) {
                view.findViewById<TextView>(getAccessoryTextViewId(accessoryId)).setBackgroundResource(drawableState.first)
                accessoryDrawableMap[accessoryId] = Pair(drawableState.first, false)
            }
        }
    }

    private fun getToggledDrawable(currentDrawable: Int): Int {
        return when (currentDrawable) {
            R.drawable.acc6 -> R.drawable.acc6_1
            R.drawable.acc6_1 -> R.drawable.acc6
            R.drawable.acc5 -> R.drawable.acc5_1
            R.drawable.acc5_1 -> R.drawable.acc5
            R.drawable.acc2 -> R.drawable.acc2_1
            R.drawable.acc2_1 -> R.drawable.acc2
            R.drawable.acc1 -> R.drawable.acc1_1
            R.drawable.acc1_1 -> R.drawable.acc1
            R.drawable.acc4 -> R.drawable.acc4_1
            R.drawable.acc4_1 -> R.drawable.acc4
            R.drawable.acc3 -> R.drawable.acc3_1
            R.drawable.acc3_1 -> R.drawable.acc3
            else -> currentDrawable
        }
    }

    private fun getAccessoryTextViewId(accessoryId: Int): Int {
        return when (accessoryId) {
            R.id.colar1fragment -> R.id.colar1txt
            R.id.colar2 -> R.id.colar2txt
            R.id.scarf1 -> R.id.scarf1txt
            R.id.scarf2 -> R.id.scarf2txt
            R.id.ribbon1 -> R.id.ribbon1txt
            R.id.ribbon2 -> R.id.ribbon2txt
            else -> -1
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(): thirdfragment {
            return thirdfragment()
        }
    }
}