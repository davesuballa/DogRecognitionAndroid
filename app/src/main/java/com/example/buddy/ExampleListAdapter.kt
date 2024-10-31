package com.example.buddy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import com.example.buddy.R

class ExampleListAdapter(private val context: Context, private val headingExplanationList: List<Map<String, String>>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return headingExplanationList.size
    }

    override fun getItem(position: Int): Any {
        return headingExplanationList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.custom_text, parent, false)
            holder = ViewHolder()
            holder.headingTextView = view.findViewById(R.id.textview1)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val headingExplanationMap = headingExplanationList[position]
        val heading = headingExplanationMap["Heading"]
        val explanation = headingExplanationMap["Explanation"]

        holder.headingTextView.text = heading
        
        holder.headingTextView.setOnClickListener {
            // Show a dialog with the explanation when the heading is clicked
            showExplanationDialog(context, explanation ?: "No explanation available")
        }

        return view!!
    }

    private fun showExplanationDialog(context: Context, explanation: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.card, null)

        // Find the TextView in the custom layout and set your desired font and color
        val explanationTextView = dialogView.findViewById<TextView>(R.id.text2)
        explanationTextView.text = explanation

        alertDialogBuilder.setView(dialogView)

        // Create the AlertDialog
        val alertDialog = alertDialogBuilder.create()

        // Show the dialog
        alertDialog.show()

        // Set the background of the dialog's window to be transparent
        val window = alertDialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)

    }


    private class ViewHolder {
        lateinit var headingTextView: TextView
    }
}
