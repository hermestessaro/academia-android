package com.example.academia

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.academia.controllers.AlunosLista.AllAlunosListaFragment
import com.example.academia.controllers.AlunosLista.MyAlunosListaFragment
import com.example.academia.models.AlunoModel

class PagerAdapter(fm: FragmentManager, val numOfTabs: Int): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = AllAlunosListaFragment()
            1 -> fragment = MyAlunosListaFragment()
        }

        return fragment
    }

    override fun getCount(): Int {
      return numOfTabs
    }
}