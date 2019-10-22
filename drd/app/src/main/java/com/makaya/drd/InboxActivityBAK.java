package com.makaya.drd;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.Document;
import com.makaya.drd.domain.DocumentLite;
import com.makaya.drd.domain.DocumentMember;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.ProcessActivity;
import com.makaya.drd.domain.Rotation;
import com.makaya.drd.domain.RotationNode;
import com.makaya.drd.domain.RotationNodeDoc;
import com.makaya.drd.domain.RotationNodeUpDoc;
import com.makaya.drd.domain.UploadResult;
import com.makaya.drd.library.FilePath;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.library.UploadImage;
import com.makaya.drd.service.DocumentService;
import com.makaya.drd.service.RotationService;
import com.makaya.drd.service.UploadFile;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by xbudi on 31/10/2016.
 */

public class InboxActivityBAK extends AppCompatActivity {

    Activity activity;

    SessionManager session;
    MemberLogin user;

    RotationNode rotationNode;
    ArrayList<RotationNodeDoc> documents=new ArrayList<>();
    ArrayList<RotationNodeUpDoc> attachments=new ArrayList<>();
    RotationNodeDoc focusNodeDoc;
    RotationNodeUpDoc focusNodeUpDoc;
    Rotation rotation;
    DocumentAdapter docAdapter;
    AttachmentAdapter attAdapter;

    TextInputEditText value;
    TextInputEditText remark;
    RecyclerView rvDoc;
    RecyclerView rvAttach;
    ImageView addDocument;
    ImageView addAttachment;
    Button btnRevisi;
    Button btnReject;
    Button btnSubmit;
    Button btnAlter;

    long dataId;

    PopupProgress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_rotation);
        activity=this;
        progress=new PopupProgress(activity);

        dataId=getIntent().getLongExtra("DataId",0);
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        PublicFunction.setHeaderStatus(activity,"Inbox");
        initObject();
        bindObject();
        fetchRotation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == 1) {
                DocumentLite lite = (DocumentLite)data.getSerializableExtra("Document");
                for(RotationNodeDoc rnd:documents){
                    if (rnd.DocumentId==lite.Id)
                        return;
                }
                RotationNodeDoc doc=new RotationNodeDoc();
                doc.Document=new Document();
                doc.FlagAction=0;
                doc.DocumentId=lite.Id;
                doc.Document.ExtFile=lite.ExtFile;
                doc.Document.CompanyId=lite.CompanyId;
                doc.Document.Descr=lite.Descr;
                doc.Document.FileName=lite.FileName;
                doc.Document.FileNameOri=lite.FileNameOri;
                doc.Document.Title=lite.Title;
                doc.Document.DocumentMembers=lite.DocumentMembers;
                focusNodeDoc=doc;
                getDocument(1, doc.DocumentId);
            }else if (requestCode == 2){
                if(data == null){
                    Toast.makeText(this,"No data present",Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri selectedFileUri = data.getData();
                String selectedFilePath = FilePath.getPath(this,selectedFileUri);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    int posX=selectedFilePath.lastIndexOf('/');
                    String filename=selectedFilePath.substring(posX+1);
                    for(RotationNodeUpDoc rnd:attachments){
                        if (rnd.DocumentUpload.FileNameOri.equals(filename))
                            return;
                    }
                    posX=filename.lastIndexOf('.');
                    String ext="";
                    if (posX!=-1)
                        ext=filename.substring(posX+1);
                    RotationNodeUpDoc doc=new RotationNodeUpDoc();
                    doc.DocumentUpload.FileNameOri=filename;
                    doc.DocumentUpload.ExtFile=ext;
                    focusNodeUpDoc=doc;
                    attachments.add(doc);
                    attAdapter.notifyDataSetChanged();
                    progress.show();
                    progress.setTextProgress("Uploading...");
                    //uploadImage(selectedFilePath); //running well but slow
                    uploadImage3(selectedFilePath); // running well but don't know receiver cant receive data
                    /*uploadFile2(MainApplication.getUrlApplWeb()+"/Home/UploadDocFile?idx=0&fileType=0",
                            selectedFilePath);*/
                    /*uploadFile(MainApplication.getUrlApplWeb()+"/Home/UploadDocFile?idx=0&fileType=0",
                            selectedFilePath);*/

                    /*String ret=XFile.uploadFileToServer(selectedFilePath,
                            MainApplication.getUrlApplWeb()+"/Home/UploadDocFile?idx=0&fileType=0");*/

                    progress.dismiss();

                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }else if (requestCode == 3){

            }

        }
    }

    private void uploadImage(String selectedFilePath) {
        int posX=selectedFilePath.lastIndexOf('/');
        String filename=selectedFilePath.substring(posX+1);
        posX=filename.lastIndexOf('.');
        String ext="";
        if (posX!=-1)
            ext=filename.substring(posX+1);

        UploadImage ui=new UploadImage();
        ui.doFileUpload(MainApplication.getUrlApplWeb()+"/Home/UploadFileFromAndroid?idx=0&fileType=0&ext="+ext,
                selectedFilePath, handler, 3);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Log.i(TAG, "Handler " + msg.what);
            progress.dismiss();
            if (msg.what == 3) {
                UploadResult obj=PublicFunction.jsonToClass(msg.obj.toString(), UploadResult.class);
                focusNodeUpDoc.DocumentUpload.FileName=obj.filename;
            } else {
                //popupProgress.dismiss();
            }
        }

    };

    public void delDocument(long id){
        int i=0;
        for(RotationNodeDoc rnd:documents){
            if (rnd.DocumentId==id) {
                documents.remove(i);
                docAdapter.notifyDataSetChanged();
                return;
            }
            i++;
        }
    }
    public void delAttachment(String filename){
        int i=0;
        for(RotationNodeUpDoc rnd:attachments){
            if (rnd.DocumentUpload.FileNameOri.equals(filename)) {
                attachments.remove(i);
                attAdapter.notifyDataSetChanged();
                return;
            }
            i++;
        }
    }
    private void bindDocRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rvDoc);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        docAdapter = new DocumentAdapter(activity, documents);
        rv.setAdapter(docAdapter);
    }
    private void bindAttchRecyclerView()
    {
        RecyclerView rv = findViewById(R.id.rvAttach);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(activity.getBaseContext());
        rv.setLayoutManager(llm);

        attAdapter = new AttachmentAdapter(activity, attachments);
        rv.setAdapter(attAdapter);
    }

    private void fetchRotation() {
        RotationService svr=new RotationService(activity);
        svr.setOnDataPostedListener(new RotationService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                if (data!=null) {
                    rotation = (Rotation) data;
                    // get current node
                    for(RotationNode rn:rotation.RotationNodes){
                        if (rn.Id==dataId){
                            rotationNode=rn;
                            break;
                        }
                    }
                    // get documents
                    for(RotationNode rn:rotation.RotationNodes){
                        if (rn.Id==rotationNode.SenderRotationNodeId){
                            documents=rn.RotationNodeDocs;
                            attachments=rn.RotationNodeUpDocs;
                            break;
                        }
                    }
                    // arrange button
                    if ((rotation.FlagAction & MainApplication.EnumActivityAction.SUBMIT.getValue())!=MainApplication.EnumActivityAction.SUBMIT.getValue())
                        btnSubmit.setVisibility(View.GONE);
                    if ((rotation.FlagAction & MainApplication.EnumActivityAction.REVISI.getValue())!=MainApplication.EnumActivityAction.REVISI.getValue())
                        btnRevisi.setVisibility(View.GONE);
                    if ((rotation.FlagAction & MainApplication.EnumActivityAction.REJECT.getValue())!=MainApplication.EnumActivityAction.REJECT.getValue())
                        btnReject.setVisibility(View.GONE);
                    if ((rotation.FlagAction & MainApplication.EnumActivityAction.ALTER.getValue())!=MainApplication.EnumActivityAction.ALTER.getValue())
                        btnAlter.setVisibility(View.GONE);
                    if (rotation.DecissionInfo.equals(""))
                        value.setVisibility(View.GONE);

                    PublicFunction.setHeaderStatus(activity,"Inbox: "+rotationNode.WorkflowNode.Caption);

                    bindDocRecyclerView();
                    bindAttchRecyclerView();
                }
            }

            @Override
            public <T> void onDataError() {
            }
        });
        svr.getNodeById(dataId);
    }

    void initObject() {
        value=findViewById(R.id.value);
        remark=findViewById(R.id.remark);
        rvDoc=findViewById(R.id.rvDoc);
        rvAttach=findViewById(R.id.rvAttach);
        addDocument=findViewById(R.id.addDocument);
        addAttachment=findViewById(R.id.addAttachment);
        btnRevisi=findViewById(R.id.btnRevisi);
        btnReject=findViewById(R.id.btnReject);
        btnSubmit=findViewById(R.id.btnSubmit);
        btnAlter=findViewById(R.id.btnAlter);

        addDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, DocumentListActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        addAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("* /* ");
                activity.startActivityForResult(getIntent, 2);*/
                showFileChooser();
            }
        });
        btnRevisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitActivity(4);
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitActivity(2);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitActivity(1);
            }
        });
        btnAlter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitActivity(8);
            }
        });
    }

    public void bindObject() {
    }
    public boolean validationBit(int val, int... bits) {
        for(int i=0;i<bits.length;i++)
        {
            if ((val & bits[i])==bits[i])
                return true;
        }
        return false;
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Attach.."),2);
    }
    private void uploadImage3(String filePath) {
        UploadFile posData = new UploadFile();
        posData.execute(MainApplication.getUrlApplWeb()+"/Home/UploadDocFile?idx=0&fileType=0",filePath);
        /*posData.setOnDataPostedListener(new UploadFile.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {

            }

            @Override
            public <T> void onPostedError(Exception data) {

            }
        });*/


    }


    private void submitActivity(int bit) {
        ProcessActivity param=new ProcessActivity();
        ArrayList<RotationNodeDoc> rotNodeDocs=new ArrayList<>();
        ArrayList<RotationNodeUpDoc> rotNodeUpDocs=new ArrayList<>();

        param.RotationNodeId=rotation.RotationNodeId;
        param.Remark=remark.getText().toString();
        param.Value=value.getText().toString();
        param.RotationNodeDocs=documents;
        param.RotationNodeUpDocs=attachments;
        int i=0;

        RotationService rsvr=new RotationService(activity);
        rsvr.setOnDataPostedListener(new RotationService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        rsvr.processActivity(param, bit);
    }
    private void getDocument(long rotationNodeId, long documentId){
        DocumentService svr=new DocumentService(activity);
        svr.setOnDataPostedListener(new DocumentService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                focusNodeDoc.Document=(Document)data;
                focusNodeDoc.Document.DocumentMember=new DocumentMember();
                for(DocumentMember dm:focusNodeDoc.Document.DocumentMembers){
                    if (dm.MemberId==user.Id) {
                        focusNodeDoc.Document.DocumentMember=dm;
                        break;
                    }
                }
                documents.add(focusNodeDoc);
                docAdapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.getDocument(1,rotationNodeId, documentId);
    }

    public void showBrowser(String url){
        Intent intent=new Intent(activity, MiniBrowserActivity.class);
        intent.putExtra("URL", url);
        intent.putExtra("Title", "PDF Viewer");
        startActivity(intent);
    }

    public int uploadFile(String strUrl, final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){

            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(strUrl.replace(" ","%20"));
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("MyImages",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=MyImages; filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                //Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(activity, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            //dialog.dismiss();
            return serverResponseCode;
        }

    }

    public int uploadFile2(String strUrl, String sourceFileUri) {
        String fileName = sourceFileUri;
        int serverResponseCode = 0;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            /*dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    +uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :"
                            +uploadFilePath + "" + uploadFileName);
                }
            });*/

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(strUrl.replace(" ","%20"));

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("MyImages", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=MyImages;filename="+ fileName + lineEnd);

                        dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            /*String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://www.androidexample.com/media/uploads/"
                                    +uploadFileName;

                            messageText.setText(msg);*/
                            Toast.makeText(activity, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                /*dialog.dismiss();
                ex.printStackTrace();*/

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(activity, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                /*dialog.dismiss();
                e.printStackTrace();*/

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(activity, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                /*Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);*/
            }
            //dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<RotationNodeDoc> datas;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView filename;
            TextView del;
            Button btnSign;
            Button btnRevisi;
            Button btnView;

            public ContentViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                filename = itemView.findViewById(R.id.filename);
                btnSign = itemView.findViewById(R.id.btnSign);
                btnRevisi = itemView.findViewById(R.id.btnRevisi);
                btnView = itemView.findViewById(R.id.btnView);
                del = itemView.findViewById(R.id.del);

                btnSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RotationNodeDoc pack=(RotationNodeDoc)view.getTag();
                        btnSign.setText("Signed");
                        pack.FlagAction|=1;
                    }
                });
                btnRevisi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RotationNodeDoc pack=(RotationNodeDoc)view.getTag();
                        btnRevisi.setText("Revised");
                        pack.FlagAction|=2;
                        showBrowser(MainApplication.getUrlApplWeb()+ "/Document/XPdfViewer?documentId="+pack.DocumentId+"&memberId="+user.Id+"&type=1");
                    }
                });
                btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RotationNodeDoc pack=(RotationNodeDoc)view.getTag();
                        btnView.setText("Viewed");
                        pack.FlagAction|=4;
                        showBrowser(MainApplication.getUrlApplWeb()+ "/Document/XPdfViewer?documentId="+pack.DocumentId+"&memberId="+user.Id+"&type=2");

                    }
                });
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RotationNodeDoc pack=(RotationNodeDoc)view.getTag();
                        ((InboxActivityBAK)activity).delDocument(pack.DocumentId);
                    }
                });
            }
        }

        public DocumentAdapter(Activity activity, ArrayList<RotationNodeDoc> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<RotationNodeDoc> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inbox_rotation_document, viewGroup, false);
            return new ContentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ContentViewHolder holder, int i) {
            RotationNodeDoc pack=datas.get(i);
            holder.title.setTag(pack);
            holder.title.setText(pack.Document.Title);
            holder.filename.setText(pack.Document.FileNameOri);

            holder.btnSign.setTag(pack);
            holder.btnRevisi.setTag(pack);
            holder.btnView.setTag(pack);
            holder.del.setTag(pack);

            if ((pack.FlagAction & 1)==1){
                holder.btnSign.setText("Signed");
            }else{
                holder.btnSign.setText("Sign");
            }
            if ((pack.FlagAction & 2)==2){
                holder.btnRevisi.setText("Revised");
            }else{
                holder.btnRevisi.setText("Revisi");
            }
            if ((pack.FlagAction & 4)==4){
                holder.btnView.setText("Viewed");
            }else{
                holder.btnView.setText("View");
            }

            int val=pack.Document.DocumentMember.FlagPermission;
            holder.btnSign.setVisibility(((val & 1)==1)?View.VISIBLE:View.GONE);
            holder.btnRevisi.setVisibility(((val & 2)==2)?View.VISIBLE:View.GONE);
            holder.btnView.setVisibility(validationBit(val,4,8,16)?View.VISIBLE:View.GONE);

        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<RotationNodeUpDoc> datas;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            TextView filename;
            TextView del;

            public ContentViewHolder(View itemView) {
                super(itemView);
                filename = itemView.findViewById(R.id.filename);
                del = itemView.findViewById(R.id.del);

                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RotationNodeUpDoc pack=(RotationNodeUpDoc)view.getTag();
                        ((InboxActivityBAK)activity).delAttachment(pack.DocumentUpload.FileNameOri);
                    }
                });
            }
        }

        public AttachmentAdapter(Activity activity, ArrayList<RotationNodeUpDoc> datas) {
            this.activity=activity;
            this.datas = datas;
        }

        public void setData(ArrayList<RotationNodeUpDoc> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inbox_rotation_attachment, viewGroup, false);
            return new ContentViewHolder(v);

        }

        @Override
        public void onBindViewHolder(final ContentViewHolder holder, int i) {

            RotationNodeUpDoc pack=datas.get(i);
            holder.filename.setTag(pack);
            holder.filename.setText(pack.DocumentUpload.FileNameOri);
            holder.del.setTag(pack);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

}
