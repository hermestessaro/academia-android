package com.example.academia.GruposLista

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.academia.R
import org.w3c.dom.Text

class ExerciciosAdapter internal constructor(private val context: Context, private val titleList: List<String>, private val dataList: HashMap<String, List<String>>)
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
            convertView = layoutInflater.inflate(R.layout.exercicio_list_group, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.exerciciosListTitle)
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
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if(convertView == null){
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.exercicio_list_item, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.expandedGrupoListItem)
        expandedListTextView.text = expandedListText
        return convertView
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }
}