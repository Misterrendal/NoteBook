package com.rendal.notebook.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.rendal.notebook.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NotesFragment.OnFragmentChange {
    override fun onFragmentChange(fragment: Fragment) {
        val listFragment = supportFragmentManager.findFragmentByTag("fragment_list")
        val fm = supportFragmentManager.beginTransaction()
        if (listFragment != null)
            fm.hide(listFragment)
        fm
            .add(fragmentContainer.id, fragment)
            .addToBackStack("main")
            .commitAllowingStateLoss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(fragmentContainer.id, NotesFragment(), "fragment_list")
                .commitAllowingStateLoss()
        }
    }
}
