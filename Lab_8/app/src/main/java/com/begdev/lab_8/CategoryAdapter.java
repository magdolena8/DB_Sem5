package com.begdev.lab_8;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private static ArrayList<Category> categoriesArrayList;
    private final LayoutInflater inflater;

    CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.categoriesArrayList = categories;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        String category = categoriesArrayList.get(position).getTitle();
        holder.title.setText(category);
    }

    @Override
    public int getItemCount() {
        return categoriesArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title_category);
        }
    }
}
