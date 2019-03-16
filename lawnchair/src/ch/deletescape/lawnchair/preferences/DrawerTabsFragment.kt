/*
 *     This file is part of Lawnchair Launcher.
 *
 *     Lawnchair Launcher is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Lawnchair Launcher is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Lawnchair Launcher.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.deletescape.lawnchair.preferences

import android.os.Bundle
import android.support.annotation.Keep
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import ch.deletescape.lawnchair.LawnchairPreferences
import ch.deletescape.lawnchair.colors.ColorEngine
import ch.deletescape.lawnchair.lawnchairPrefs
import ch.deletescape.lawnchair.tintDrawable
import com.android.launcher3.R

@Keep
class DrawerTabsFragment : RecyclerViewFragment(), LawnchairPreferences.OnPreferenceChangeListener {

    private var adapter: DrawerTabsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(container!!.context).inflate(R.layout.fragment_drawer_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.btn_add).apply {
            tintDrawable(ColorEngine.getInstance(context).accent)
            setOnClickListener {
                DrawerTabEditBottomSheet.newTab(activity!!) {
                    adapter!!.addTab(it)
                }
            }
        }
    }

    override fun onRecyclerViewCreated(recyclerView: RecyclerView) {
        val context = recyclerView.context
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = DrawerTabsAdapter(context).apply {
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        context!!.lawnchairPrefs.addOnPreferenceChangeListener("pref_drawerTabs", this)
    }

    override fun onPause() {
        super.onPause()

        context!!.lawnchairPrefs.removeOnPreferenceChangeListener("pref_drawerTabs", this)
        adapter?.saveChanges()
    }

    override fun onValueChanged(key: String, prefs: LawnchairPreferences, force: Boolean) {
        adapter!!.reloadTabs()
    }
}
