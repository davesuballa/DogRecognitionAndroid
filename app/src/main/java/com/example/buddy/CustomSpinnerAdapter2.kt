package com.example.buddy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class CustomSpinnerAdapter2(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(inflater, position, convertView, parent, android.R.layout.simple_spinner_item)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(inflater, position, convertView, parent, R.layout.spinner_item2)
    }

    private fun createViewFromResource(
        inflater: LayoutInflater,
        position: Int,
        convertView: View?,
        parent: ViewGroup,
        resource: Int
    ): View {
        val view = convertView ?: inflater.inflate(resource, parent, false)

        // Find the TextView inside the inflated view
        val textView = view.findViewById<TextView>(android.R.id.text1)

        // Set the text for the TextView
        textView.text = getItem(position)

        // Apply custom font to the TextView
        val customFont = ResourcesCompat.getFont(context, R.font.featherbold)
        textView.typeface = customFont

        textView.setTextColor(ContextCompat.getColor(context, R.color.brownspinner))
        return view
    }

}
