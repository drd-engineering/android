package com.makaya.drd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.makaya.drd.domain.Document;
import com.makaya.drd.domain.DocumentLite;
import com.makaya.drd.domain.MemberLogin;
import com.makaya.drd.domain.ProcessActivity;
import com.makaya.drd.domain.Rotation;
import com.makaya.drd.domain.RotationNode;
import com.makaya.drd.domain.RotationNodeDoc;
import com.makaya.drd.domain.RotationNodeUpDoc;
import com.makaya.drd.domain.UploadResult;
import com.makaya.drd.library.FilePath;
import com.makaya.drd.library.PublicFunction;
import com.makaya.drd.service.DocumentService;
import com.makaya.drd.service.MemberService;
import com.makaya.drd.service.RotationService;
import com.makaya.drd.service.UploadFile;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by xbudi on 31/10/2016.
 */

public class InboxActivity extends AppCompatActivity {

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

    LinearLayout rotationPanel;
    TextView rotationname;
    TextView workflow;
    TextView status;
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
    CardView mainLayout;
    ProgressBar progressBar;
    long nodeId;
    long doxId;

    PopupProgressUpload progress;
    UploadFile upload;
    PopupProgress progressPwd;
    PopupProgress progresssubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_rotation);
        activity=this;
        progress=new PopupProgressUpload(activity);
        progress.setOnListener(new PopupProgressUpload.OnSetListener() {
            @Override
            public <T> void onProcessCanceled() {
                upload.throwCancel();
            }
        });
        progressPwd=new PopupProgress(activity);
        progresssubmit=new PopupProgress(activity);
        nodeId =getIntent().getLongExtra("NodeId",0);
        session=new SessionManager(getApplication());
        user=session.getUserLogin();
        PublicFunction.setHeaderStatus(activity,"Inbox");
        initObject();
        fetchRotation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            // add document
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
                getDocument(nodeId, doc.DocumentId);
                doxId=doc.DocumentId;
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
                    uploadFile(selectedFilePath);

                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }else if (requestCode == 3){
                getDocumentPermission(nodeId, doxId);
            }
        }
    }

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
                        if (rn.Id== nodeId){
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
                    bindObject();
                    bindDocRecyclerView();
                    bindAttchRecyclerView();
                    progressBar.setVisibility(View.GONE);
                    rotationPanel.setVisibility(View.VISIBLE);
                    mainLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public <T> void onDataError() {
            }
        });
        svr.getNodeById(nodeId);
    }

    void initObject() {
        rotationPanel=findViewById(R.id.rotationPanel);
        rotationname=findViewById(R.id.rotationname);
        workflow=findViewById(R.id.workflow);
        status=findViewById(R.id.status);
        mainLayout=findViewById(R.id.mainLayout);

        rotationPanel.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        progressBar=findViewById(R.id.progressBar);
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

        rotationPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, ProgressRotationDetailActivity.class);
                intent.putExtra("RotationId", rotation.Id);
                intent.putExtra("RotationStatus", "01");
                intent.putExtra("Caption", rotation.Subject);
                startActivity(intent);
            }
        });

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
        rotationname.setText(rotation.Subject);
        workflow.setText(rotation.Workflow.Name);
        status.setText(Html.fromHtml("<b>"+rotation.StatusDescr+"</b> at <i>"+PublicFunction.dateToString("dd MMM yyyy HH:mm:ss",rotation.DateStatus)+"</i>"));
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
    private void uploadFile(String filePath) {
        progress.show();
        progress.setFilename(focusNodeUpDoc.DocumentUpload.FileNameOri);
        progress.setTitle("Uploading...");
        upload=new UploadFile();
        upload.execute(MainApplication.getUrlApplWeb()+"/updownfile/xupload?idx=0&fileType=0",filePath);
        upload.setOnDataPostedListener(new UploadFile.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(final T data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        UploadResult ur=(UploadResult)data;
                        if (ur.idx==-1) {
                            //Toast.makeText(activity, "Error: " + ur.message, Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(activity)
                                    .setTitle("Error")
                                    .setMessage(ur.message)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //finish();
                                        }
                                    }).show();
                        }if (ur.idx==-2) {
                            upload.cancel(true);
                            Toast.makeText(activity, "Upload file canceled", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(activity, "Upload file successfully", Toast.LENGTH_SHORT).show();
                            attachments.add(focusNodeUpDoc);
                            attAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }

            @Override
            public <T> void onPostedError(final Exception data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        new AlertDialog.Builder(activity)
                                .setTitle("Error")
                                .setMessage(data.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //finish();
                                    }
                                }).show();
                    }
                });

            }

            @Override
            public <T> void onProgress(final long totalSize, final long processSize) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setProcess(
                                NumberFormat.getInstance().format(processSize)+" to "+
                                NumberFormat.getInstance().format(totalSize));
                    }
                });
            }
        });


    }
    private void submitActivity(int bit) {

        progresssubmit.show();

        ProcessActivity param=new ProcessActivity();

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
                progresssubmit.dismiss();
                if ((Integer)data==-1){
                    new AlertDialog.Builder(activity)
                            .setTitle("Warning")
                            .setMessage("Transfer cannot be processed, balance insufficient of the sender. Please contact the sender")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            }).show();
                    return;
                }
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public <T> void onDataError() {
                progresssubmit.dismiss();
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
                //focusNodeDoc.Document.DocumentMember=focusNodeDoc.Document.DocumentMember;//new DocumentMember();
                /*for(DocumentMember dm:focusNodeDoc.Document.DocumentMembers){
                    if (dm.MemberId==user.Id) {
                        focusNodeDoc.Document.DocumentMember=dm;
                        break;
                    }
                }*/
                documents.add(focusNodeDoc);
                docAdapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.getDocument(user.Id, rotationNodeId, documentId);
    }

    private void getDocumentPermission(long rotationNodeId, long documentId){
        DocumentService svr=new DocumentService(activity);
        svr.setOnDataPostedListener(new DocumentService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                focusNodeDoc.Document.DocumentMember.FlagPermission=(Integer)data;
                docAdapter.notifyDataSetChanged();
            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.getDocumentPermission(user.Id, rotationNodeId, documentId);
    }

    public void showBrowser(String url, int viewType, int requestCode){
        Intent intent=new Intent(activity, DocRevisiBrowserActivity.class);
        intent.putExtra("URL", url);
        intent.putExtra("Title", "PDF Viewer");
        intent.putExtra("ViewType", viewType);
        startActivityForResult(intent, requestCode);
    }

    public void validationPasswordSign(String password){
        MemberService svr=new MemberService(activity);
        svr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {

            }

            @Override
            public <T> void onDataError() {

            }
        });
        svr.validationPassword(user.Id, password);
    }

    public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ContentViewHolder> {

        Activity activity;
        ArrayList<RotationNodeDoc> datas;

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView filename;
            ImageView del;
            ImageView btnSign;
            ImageView btnStamp;
            ImageView btnRevisi;
            ImageView btnView;

            public ContentViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                filename = itemView.findViewById(R.id.filename);
                btnSign = itemView.findViewById(R.id.btnSign);
                btnStamp = itemView.findViewById(R.id.btnStamp);
                btnRevisi = itemView.findViewById(R.id.btnRevisi);
                btnView = itemView.findViewById(R.id.btnView);
                del = itemView.findViewById(R.id.del);

                btnSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        RotationService rsvr=new RotationService(activity);
                        rsvr.setOnDataPostedListener(new RotationService.OnSetDataPostedListener() {
                            @Override
                            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                                if ((Integer)data==-1){
                                    new AlertDialog.Builder(activity)
                                            .setTitle("Error")
                                            .setMessage("Check your registration form. Completed your field ( Id Number/foto ID) or signature/initial.")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //finish();
                                                }
                                            }).show();
                                    return;
                                }

                                PopupPassword pwd=new PopupPassword(activity);
                                pwd.setOnButtonSelectedListener(new PopupPassword.OnButtonSelectedListener() {
                                    @Override
                                    public void onSubmit(String pwd) {
                                        progressPwd.show();
                                        MemberService svr=new MemberService(activity);
                                        svr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
                                            @Override
                                            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                                                progressPwd.dismiss();
                                                if (!((Boolean)data)){
                                                    Toast.makeText(activity,"Invalid password",Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                RotationNodeDoc pack=(RotationNodeDoc)view.getTag();
                                                btnSign.setImageResource(R.drawable.ic_signed);
                                                pack.FlagAction|=1;
                                            }

                                            @Override
                                            public <T> void onDataError() {
                                                progressPwd.dismiss();
                                            }
                                        });
                                        svr.validationPassword(user.Id, pwd);
                                    }

                                    @Override
                                    public void onClose() {

                                    }
                                });
                                pwd.show();

                            }

                            @Override
                            public <T> void onDataError() {

                            }
                        });
                        rsvr.checkingSignature(user.Id);
                    }
                });
                btnStamp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        RotationService rsvr=new RotationService(activity);
                        rsvr.setOnDataPostedListener(new RotationService.OnSetDataPostedListener() {
                            @Override
                            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                                if ((Integer)data==-1){
                                    new AlertDialog.Builder(activity)
                                            .setTitle("Error")
                                            .setMessage("Check your registration form. Completed your field private stamp image.")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //finish();
                                                }
                                            }).show();
                                    return;
                                }

                                PopupPassword pwd=new PopupPassword(activity);
                                pwd.setOnButtonSelectedListener(new PopupPassword.OnButtonSelectedListener() {
                                    @Override
                                    public void onSubmit(String pwd) {
                                        progressPwd.show();
                                        MemberService svr=new MemberService(activity);
                                        svr.setOnDataPostedListener(new MemberService.OnSetDataPostedListener() {
                                            @Override
                                            public <T> void onDataPosted(T data, boolean isLoadedAllItems) {
                                                progressPwd.dismiss();
                                                if (!((Boolean)data)){
                                                    Toast.makeText(activity,"Invalid password",Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                RotationNodeDoc pack=(RotationNodeDoc)view.getTag();
                                                btnStamp.setImageResource(R.drawable.ic_stamped);
                                                pack.FlagAction|=32;
                                            }

                                            @Override
                                            public <T> void onDataError() {
                                                progressPwd.dismiss();
                                            }
                                        });
                                        svr.validationPassword(user.Id, pwd);
                                    }

                                    @Override
                                    public void onClose() {

                                    }
                                });
                                pwd.show();
                            }

                            @Override
                            public <T> void onDataError() {

                            }
                        });
                        rsvr.checkingPrivateStamp(user.Id);
                    }
                });
                btnRevisi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RotationNodeDoc pack=(RotationNodeDoc)view.getTag();
                        btnRevisi.setImageResource(R.drawable.ic_revised);
                        pack.FlagAction|=2;
                        showBrowser(MainApplication.getUrlApplWeb()+ "/Document/XPdfViewer?documentId="+pack.DocumentId+"&memberId="+user.Id+"&type=1", 1, 3);
                    }
                });
                btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RotationNodeDoc pack=(RotationNodeDoc)view.getTag();
                        btnView.setImageResource(R.drawable.ic_viewed);
                        pack.FlagAction|=4;
                        showBrowser(MainApplication.getUrlApplWeb()+ "/Document/XPdfViewer?documentId="+pack.DocumentId+"&memberId="+user.Id+"&type=2", 2, 0);

                    }
                });
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RotationNodeDoc pack=(RotationNodeDoc)view.getTag();
                        ((InboxActivity)activity).delDocument(pack.DocumentId);
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
            holder.btnStamp.setTag(pack);
            holder.btnRevisi.setTag(pack);
            holder.btnView.setTag(pack);
            holder.del.setTag(pack);

            if ((pack.FlagAction & 1)==1){
                holder.btnSign.setImageResource(R.drawable.ic_signed);
            }else{
                holder.btnSign.setImageResource(R.drawable.ic_sign);
            }
            if ((pack.FlagAction & 32)==32){
                holder.btnStamp.setImageResource(R.drawable.ic_stamped);
            }else{
                holder.btnStamp.setImageResource(R.drawable.ic_stamp);
            }
            if ((pack.FlagAction & 2)==2){
                holder.btnRevisi.setImageResource(R.drawable.ic_revised);
            }else{
                holder.btnRevisi.setImageResource(R.drawable.ic_revisi);
            }
            if ((pack.FlagAction & 4)==4){
                holder.btnView.setImageResource(R.drawable.ic_viewed);
            }else{
                holder.btnView.setImageResource(R.drawable.ic_view);
            }

            int val=pack.Document.DocumentMember.FlagPermission;
            holder.btnSign.setVisibility(((val & 1)==1)?View.VISIBLE:View.GONE);
            holder.btnStamp.setVisibility(((val & 32)==32)?View.VISIBLE:View.GONE);
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
            ImageView del;

            public ContentViewHolder(View itemView) {
                super(itemView);
                filename = itemView.findViewById(R.id.filename);
                del = itemView.findViewById(R.id.del);

                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RotationNodeUpDoc pack=(RotationNodeUpDoc)view.getTag();
                        ((InboxActivity)activity).delAttachment(pack.DocumentUpload.FileNameOri);
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
