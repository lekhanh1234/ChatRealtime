package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messenger.R;
import com.example.messenger.operationGallary.OperationGallary;

import java.util.ArrayList;
import java.util.List;

public class ADTAllGallaryPicture extends RecyclerView.Adapter<ADTAllGallaryPicture.viewhold> {
    private Context context;
    private final ArrayList<String> listURLPicture;
    private final ArrayList<Button> selectPictureIndex = new ArrayList<>();
    private final ArrayList<byte[]> pictureQueue = new ArrayList<>();
    private final Button BTN_SendPicture;

    public ArrayList<byte[]> getPictureQueue() {
        return pictureQueue;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ADTAllGallaryPicture(Context context, List<String> listURLPicture) {
        this.context = context;
        this.listURLPicture = (ArrayList<String>)listURLPicture;
        BTN_SendPicture = ((Activity)context).findViewById(R.id.BT_sendPicture);

    }


    public ADTAllGallaryPicture.viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_gallary_for_message, parent, false);
        return new ADTAllGallaryPicture.viewhold(view);
    }

    @Override
    public int getItemCount() {
        return listURLPicture.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ADTAllGallaryPicture.viewhold holder, int position) {
        ImageView imageView=holder.itemView.findViewById(R.id.IMG_picture_gallary);
        Button BT_index = holder.itemView.findViewById(R.id.BT_index);
        byte[] a = new OperationGallary().convertPictureURLToByteArray(listURLPicture.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectPictureIndex.contains(BT_index)){
                    // neu ton tai phan tu. xoa khoi hang doi va an no di tren screen
                    BT_index.setVisibility(View.GONE);
                    selectPictureIndex.remove(BT_index);
                    pictureQueue.remove(a);
                    // kiem tra. neu pictureQueue trong thi an imageview di
                    if(pictureQueue.size()==0){
                        BTN_SendPicture.setVisibility(View.GONE);
                    }
                }
                else {
                    selectPictureIndex.add(BT_index);
                    BT_index.setVisibility(View.VISIBLE);
                    // ta se chuyen tu file trong thu vien thanh mang byte
                    pictureQueue.add(a);
                    Button BT_sendPicture = ((Activity)context).findViewById(R.id.BT_sendPicture);
                    BT_sendPicture.setVisibility(View.VISIBLE);
                }
                // cuoi  cung la danh so lai BT_index
                Log.d("098", "onClick: ");
                int index = 0;
                for(Button x : selectPictureIndex){
                    x.setText(String.valueOf(++index));
                }
            }
        });
        Glide.with(context)
                .load(listURLPicture.get(position))
                .into(imageView);
    }
    class viewhold extends RecyclerView.ViewHolder{
        public viewhold(@NonNull View itemView) {
            super(itemView);
        }
    }
}
