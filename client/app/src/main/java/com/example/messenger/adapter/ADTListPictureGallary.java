package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.messenger.R;
import com.example.messenger.ui.PickPictureToAvatar;
import java.util.ArrayList;


public class ADTListPictureGallary extends RecyclerView.Adapter<ADTListPictureGallary.viewhold> {
    private Context context;
    private final ArrayList<String> listURLPicture;

    public Context getContext() {
        return context;
    }
    private ImageView lopphu;


    public void setContext(Context context) {
        this.context = context;
    }
    public ADTListPictureGallary(Context context, ArrayList<String> listURLPicture) {
        this.context = context;
        this.listURLPicture = listURLPicture;
    }
    @Override
    public viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.picture_from_gallery, parent, false);
        return new viewhold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewhold holder,int position) {
       ImageView imageView=holder.itemView.findViewById(R.id.IMG_picture_gallary);
        Glide.with(context)
                .load(listURLPicture.get(position))
                .into(imageView);
        ImageView lopphu=holder.itemView.findViewById(R.id.IMG_baophu);
        lopphu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ADTListPictureGallary.this.lopphu!=null){
                    ADTListPictureGallary.this.lopphu.setAlpha(0.0f);
                }
                ADTListPictureGallary.this.lopphu= lopphu;
                lopphu.setAlpha(0.7f);
                ConstraintLayout CL_setPictureProfile=((Activity)context).findViewById(R.id.CL_setPitureProfile);
                CL_setPictureProfile.setVisibility(View.VISIBLE);
                ImageView IMG_previewProfile=((Activity)context).findViewById(R.id.IMG_ImageProfilePreview);
                Glide.with(context)
                        .load(listURLPicture.get(position))
                        .into(IMG_previewProfile);
                ((PickPictureToAvatar)context).setURL_imageProfile(listURLPicture.get(position));
            }
        });

}

    @Override
    public int getItemCount() {
        return listURLPicture.size();
    }

    class viewhold extends RecyclerView.ViewHolder{

        public viewhold(@NonNull View itemView) {
            super(itemView);
        }
    }
}
