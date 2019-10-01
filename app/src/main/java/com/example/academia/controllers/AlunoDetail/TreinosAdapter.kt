package com.example.academia.controllers.AlunoDetail

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.academia.R
import com.example.academia.models.TreinoModel
import org.w3c.dom.Text

class TreinosAdapter internal constructor(private val context: Context, private val titleList: List<String>, private val dataList: HashMap<String, MutableList<TreinoModel>>)
    : BaseExpandableListAdapter(){
    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if(convertView == null){
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.treino_list_group, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.treinoListTitle)
        listTitleTextView.text = listTitle
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.titleList[listPosition]]!!.size
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val treino = getChild(listPosition, expandedListPosition) as TreinoModel
        val expandedListText = treino.nome
        if(convertView == null){
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.treino_list_item, null)
        }
        val circle = convertView!!.findViewById<ImageView>(R.id.circle)
        if(expandedListPosition == 1) {
            circle.setImageResource(R.drawable.round_identifier_green)
        }
        if(isLastChild){
            circle.setImageResource(R.drawable.round_identifier_red)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.expandedTreinoListItem)
        val tipoTextView = convertView!!.findViewById<TextView>(R.id.tipo)
        expandedListTextView.text = expandedListText
        tipoTextView.text = treino.tipo
        return convertView
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }
}
