package com.example.sweelam.dmstask.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweelam.dmstask.Models.Module;
import com.example.sweelam.dmstask.Models.Owner;
import com.example.sweelam.dmstask.R;

import java.util.ArrayList;
import java.util.List;

public class PaganationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Module> data;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private Context context;
    private boolean isLoadingAdded = false;


    public PaganationAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public List<Module> getModule() {
        return data;
    }

    public void setModule(List<Module> data) {
        this.data = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;

    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.recyclerview_row, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Module module = data.get(position);


        switch (getItemViewType(position)) {
            case ITEM:
                MovieVH movieVH = (MovieVH) holder;

                if (module.getFork())
                    movieVH.cardView.setCardBackgroundColor(Color.parseColor("#aacd96"));
                else
                    movieVH.cardView.setCardBackgroundColor(Color.WHITE);

                movieVH.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(module.getHtmlUrl()!=null&&module.getOwner().getHtmlUrl()!=null) {
                            String html_url = module.getHtmlUrl();
                            Owner owner = module.getOwner();
                            String owner_html_url = owner.getHtmlUrl();
                            alertDialog(html_url, owner_html_url);

                        }
                        return false;
                    }
                });

                movieVH.repo_name.setText(module.getName());
                movieVH.desc.setText(module.getDescription());
                movieVH.userName_owner.setText(module.getFullName());
                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    @Override
    public int getItemViewType(int position) {
        return (position == data.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

    }


    protected class MovieVH extends RecyclerView.ViewHolder {
        TextView repo_name, desc, userName_owner;
        CardView cardView;

        public MovieVH(View itemView) {
            super(itemView);

            repo_name = itemView.findViewById(R.id.text_repoName);
            desc = itemView.findViewById(R.id.text_desc);
            userName_owner = itemView.findViewById(R.id.text_userName_owner);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


    public void add(Module mc) {
        data.add(mc);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<Module> mcList) {
        for (Module mc : mcList) {
            add(mc);
        }
    }

    public void remove(Module city) {
        int position = data.indexOf(city);
        if (position > -1) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void removeALl(){
        data.clear();
        notifyDataSetChanged();

    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Module());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = data.size() - 1;
        Module item = getItem(position);

        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Module getItem(int position) {
        return data.get(position);
    }

    private void alertDialog(String html_url, String html_url_owner) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Hello")
                .setMessage("Where do You want to go !!")
                .setPositiveButton(R.string.dialog_btn1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        openLinkViaBrowser(html_url_owner);
                    }
                })
                .setNegativeButton(R.string.dialog_btn2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        openLinkViaBrowser(html_url);


                    }
                })
                /*  .setIcon(android.R.drawable.ic_dialog_alert)*/
                .show();
    }

    private void openLinkViaBrowser(String url) {

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}