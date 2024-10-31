package com.example.buddy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FirstFragment : Fragment() {

    private lateinit var breedSelectedListener: OnBreedSelectedListener
    private lateinit var sharedViewModel: SharedViewModel


    interface OnBreedSelectedListener {
        fun onBreedSelected(breedId: Int)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Live view for each breed and fur color
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        view.findViewById<Button>(R.id.gretrieverbtn).setOnClickListener {
            handleButtonClick(R.id.gretriever)
            sharedViewModel.setBreed("Golden Retriever")


            val gretrieverTextView: TextView = view.findViewById(R.id.gretrievertxt)
            val shihtzuTextView: TextView = view.findViewById(R.id.shihtzutxt)
            val poodle: TextView = view.findViewById(R.id.poodletxt)
            val pomeranian: TextView = view.findViewById(R.id.pomaraniantxt)
            val chihuahua: TextView = view.findViewById(R.id.chihuahuatxt)

            gretrieverTextView.setBackgroundResource(R.drawable.golden1_btn)
            shihtzuTextView.setBackgroundResource(R.drawable.shihtzu_light)
            poodle.setBackgroundResource(R.drawable.poodle1)
            pomeranian.setBackgroundResource(R.drawable.pomerenian1)
            chihuahua.setBackgroundResource(R.drawable.chihuahua_light)

        }

        view.findViewById<Button>(R.id.shihtzubtn).setOnClickListener {
            handleButtonClick(R.id.shitzu)
            sharedViewModel.setBreed("Shih Tzu")

            val gretrieverTextView: TextView = view.findViewById(R.id.gretrievertxt)
            val shihtzuTextView: TextView = view.findViewById(R.id.shihtzutxt)
            val poodle: TextView = view.findViewById(R.id.poodletxt)
            val pomeranian: TextView = view.findViewById(R.id.pomaraniantxt)
            val chihuahua: TextView = view.findViewById(R.id.chihuahuatxt)

            gretrieverTextView.setBackgroundResource(R.drawable.golden2_btn)
            shihtzuTextView.setBackgroundResource(R.drawable.shihtzu2)
            poodle.setBackgroundResource(R.drawable.poodle1)
            pomeranian.setBackgroundResource(R.drawable.pomerenian1)
            chihuahua.setBackgroundResource(R.drawable.chihuahua_light)

        }

        view.findViewById<Button>(R.id.poodlebtn).setOnClickListener {
            handleButtonClick(R.id.poodle)
            sharedViewModel.setBreed("Poodle")

            val gretrieverTextView: TextView = view.findViewById(R.id.gretrievertxt)
            val shihtzuTextView: TextView = view.findViewById(R.id.shihtzutxt)
            val poodle: TextView = view.findViewById(R.id.poodletxt)
            val pomeranian: TextView = view.findViewById(R.id.pomaraniantxt)
            val chihuahua: TextView = view.findViewById(R.id.chihuahuatxt)

            gretrieverTextView.setBackgroundResource(R.drawable.golden2_btn)
            poodle.setBackgroundResource(R.drawable.poodle_dark)
            pomeranian.setBackgroundResource(R.drawable.pomerenian1)
            chihuahua.setBackgroundResource(R.drawable.chihuahua_light)
            shihtzuTextView.setBackgroundResource(R.drawable.shihtzu_light)
        }

        view.findViewById<Button>(R.id.chihuahuabtn).setOnClickListener {
            val gretrieverTextView: TextView = view.findViewById(R.id.gretrievertxt)
            val shihtzuTextView: TextView = view.findViewById(R.id.shihtzutxt)
            val poodle: TextView = view.findViewById(R.id.poodletxt)
            val pomeranian: TextView = view.findViewById(R.id.pomaraniantxt)
            val chihuahua: TextView = view.findViewById(R.id.chihuahuatxt)

            gretrieverTextView.setBackgroundResource(R.drawable.golden2_btn)
            poodle.setBackgroundResource(R.drawable.poodle1)
            pomeranian.setBackgroundResource(R.drawable.pomerenian1)
            chihuahua.setBackgroundResource(R.drawable.chiuahua2)
            shihtzuTextView.setBackgroundResource(R.drawable.shihtzu_light)
            handleButtonClick(R.id.chihuahua)
            sharedViewModel.setBreed("Chihuahua")
        }

        view.findViewById<Button>(R.id.pomeranianbtn).setOnClickListener {
            handleButtonClick(R.id.pomeranian)
            sharedViewModel.setBreed("Pomeranian")

            val gretrieverTextView: TextView = view.findViewById(R.id.gretrievertxt)
            val shihtzuTextView: TextView = view.findViewById(R.id.shihtzutxt)
            val poodle: TextView = view.findViewById(R.id.poodletxt)
            val pomeranian: TextView = view.findViewById(R.id.pomaraniantxt)
            val chihuahua: TextView = view.findViewById(R.id.chihuahuatxt)

            // Set background drawable for "Golden Retriever" TextView
            gretrieverTextView.setBackgroundResource(R.drawable.golden2_btn)
            poodle.setBackgroundResource(R.drawable.poodle1)
            pomeranian.setBackgroundResource(R.drawable.pomerenian2)
            chihuahua.setBackgroundResource(R.drawable.chihuahua_light)
            shihtzuTextView.setBackgroundResource(R.drawable.shihtzu_light)

        }
    }

    private fun handleButtonClick(breedId: Int) {
        breedSelectedListener.onBreedSelected(breedId)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        breedSelectedListener = if (context is OnBreedSelectedListener) {
            context
        } else {
            throw RuntimeException("$context must implement OnBreedSelectedListener")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): FirstFragment {
            return FirstFragment()
        }
    }
}
