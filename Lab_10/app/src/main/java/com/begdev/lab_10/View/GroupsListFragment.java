package com.begdev.lab_10.View;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.begdev.lab_10.Model.Group;
import com.begdev.lab_10.R;

public class GroupsListFragment extends Fragment {
    ListView groupsListView;
    Group mSelectedGroup;

    public GroupsListFragment() {
        super(R.layout.fragment_groupslist);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        groupsListView = view.findViewById(R.id.groups_listview);
//        MainActivity qwe = (MainActivity) getActivity();
        ArrayAdapter<Group> adapter = new ArrayAdapter<Group>(getActivity(), android.R.layout.simple_spinner_item, Group.groups);
        groupsListView.setAdapter(adapter);
        groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedGroup = adapter.getItem(i);
                Fragment selectedFragment = new StudentsListFragment(mSelectedGroup);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer_main, selectedFragment).addToBackStack(null).commit();
            }
        });
        groupsListView.setOnItemLongClickListener(groupLongClickListener);
    }

    AdapterView.OnItemLongClickListener groupLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            showPopupMenu(view, (Group)adapterView.getItemAtPosition(i));
            return true;
        }
    };

    private void showPopupMenu(View v, Group group) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.inflate(R.menu.delete_popup_menu);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_menu_btn: {
                                Group.deleteGroup(getActivity(), group);
                                return true;
                            }
                            default:
                                return false;
                        }
                    }
                });
        popupMenu.show();
    }
}