package com.example.wayup.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wayup.CommentActivity;
import com.example.wayup.MainActivity;
import com.example.wayup.R;
import com.example.wayup.fragment.ForumFragment;
import com.example.wayup.model.Company;
import com.example.wayup.model.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompanyGridviewAdapter extends BaseAdapter implements Filterable{
    private Context context;
    private int layout;
    private List<Company> arrItem;
    private List<Company> orig;

    public CompanyGridviewAdapter(Context context, int layout, List<Company> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }
//
//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        ForumFragment.companies.clear();
//        if (charText.length() == 0) {
//            ForumFragment.companies.addAll(arrItem);
//        } else {
//            for (Company company : arrItem) {
//                if (company.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    ForumFragment.companies.add(company);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Company> results = new ArrayList<Company>();
                if (orig == null)
                    orig = arrItem;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Company g : orig) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                arrItem = (ArrayList<Company>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    private class ViewHolder{
        ImageView img;
        TextView tvTitle;
    }

    @Override
    public int getCount() {
        return arrItem.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewRow = view;
        if(viewRow == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                viewRow = inflater.inflate(layout,viewGroup,false);
            }

            ViewHolder holder = new ViewHolder();
            if (viewRow != null) {
                holder.tvTitle = viewRow.findViewById(R.id.tvNameItemGVCompany);
            }
            if (viewRow != null) {
                holder.img = viewRow.findViewById(R.id.imgLogoItemGvCompany);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final Company item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
            holder.tvTitle.setText(item.getName());
        }
        if (holder != null) {
            Glide.with(context).load(item.getThumbnail()).into(holder.img);
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra(MainActivity.KEY_PASS_DATA, item);
                context.startActivity(intent);        }
        });

        return viewRow;
    }

}