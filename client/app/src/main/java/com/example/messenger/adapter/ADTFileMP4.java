package com.example.messenger.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.example.messenger.ui.AddPreferencePost;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ADTFileMP4 extends BaseAdapter {
    private final Context context;
    private  List<String> MP4PathList;

    public void setMP4PathList(List<String> MP4PathList) {
        this.MP4PathList = MP4PathList;
    }

    private final int type; // nếu bằng 1 thì adapter này là được sử dụng trong việc video cho profile . nếu == 2 thì là up -> friend

    public ADTFileMP4(Context context, List<String> MP4PathList,int type) {
        this.context = context;
        this.MP4PathList = MP4PathList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return MP4PathList != null ? MP4PathList.size() : 0 ;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView  = ((AddPreferencePost)context).getLayoutInflater().inflate(R.layout.file_mp4,null);
        ImageView IMG_FileMP4 = convertView.findViewById(R.id.IMG_FileMP4);
        /////////////////////// set noi hinh anh cho imgview
        Glide.with(context)
                .load(MP4PathList.get(position))
                .into(IMG_FileMP4);
        ////////////////////////////////////////////////////
        Button BTN_Confirm = convertView.findViewById(R.id.BTN_Confirm);
        BTN_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInternet.isNetworkAvailable(context) == false){
                    Toast.makeText(context,"Kiểm tra kết nối Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                BTN_Confirm.setText("Đang gửi");
                // gui thong tin len server de xac nhan rang la 1 tin moi vua duoc them vao
                File fileOutput = null;
                for(int i = 1;i<100;i++){
                    String outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/aipzusrygzpziyopaie"+i+".mp4";
                    fileOutput = new File(outputPath);
                    if(!fileOutput.exists()) break;
                    if(i == 100) return;
                }
                try {
                    fileOutput.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String[] complexCommand = {"-ss", "0", "-y", "-i", MP4PathList.get(position), "-t", "15", "-s", "1280x960", "-r", "15", "-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", fileOutput.getAbsolutePath()};
                FFmpeg.execute(complexCommand);
                // gửi file đó lên server.
                if(!CheckInternet.isNetworkAvailable(context)){
                    fileOutput.delete();
                    return;
                }
                byte[] file = new byte[(int)fileOutput.length()];
                try {
                    FileInputStream fis = new FileInputStream(fileOutput);
                    fis.read(file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                fileOutput.delete();
                new OperationServer().sendPostToFriend(InfoMyAccount.getAccountId(),file,type);
                ((AddPreferencePost) context).setResult(200);
                BTN_Confirm.setOnClickListener(null);
                BTN_Confirm.setText("Đã gửi");
                BTN_Confirm.setBackgroundColor(context.getResources().getColor(R.color.color_xam_tab));
                ((AddPreferencePost) context).finish();
            }
        });
        return convertView;
    }
}
