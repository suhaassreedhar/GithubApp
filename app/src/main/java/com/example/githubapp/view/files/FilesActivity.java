package com.example.githubapp.view.files;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.protectsoft.webviewcode.Codeview;
import com.protectsoft.webviewcode.Settings;
import com.example.githubapp.R;
import com.example.githubapp.adapter.FileDirRecyclerViewAdapter;
import com.example.githubapp.adapter.PathRecyclerViewAdapter;
import com.example.githubapp.entity.response.repos.RepositoryContentBean;
import com.example.githubapp.listener.OnDirItemClickListener;
import com.example.githubapp.presenter.files.FilesContract;
import com.example.githubapp.presenter.files.FilesPresenter;
import com.example.githubapp.ui.DividerItemDecoration;
import com.example.githubapp.utils.HtmlImageGetter;
import com.example.githubapp.utils.TextUtil;
import com.example.githubapp.view.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;



public class FilesActivity extends ToolbarActivity implements FilesContract.View {
    private final String TAG = getClass().getName();


    public static final String REPO = "REPO";
    public static final String OWNER = "OWNER";
    public static final String REF = "REF";
    public static final String BRANCH = "BRANCH";
    public static final String URL = "URL";

    private FilesContract.Presenter mPresenter;

    private String repo;
    private String owner;
    private String ref;
    private String branch;
    private String url;
    private String path;

    private String sha;

    private FileDirRecyclerViewAdapter mDirAdapter;
    private PathRecyclerViewAdapter mPathAdapter;

    private String fileContent;
    private boolean isFileLoading = false;

    private SwipeRefreshLayout mSRLayout;
    private RecyclerView mPathRV;
    private RecyclerView mContentRV;
    private WebView mCodeWB;
    private LinearLayout mContentLayout;
    private NestedScrollView mFileLayout;
    private AppCompatTextView mFileTV;

    private List<RepositoryContentBean> contentList = new ArrayList<>();

    @Override
    protected void onStop() {
        mPresenter.stop();
        super.onStop();
    }

    @Override
    public void initViews() {

        getParams();

        setContent(R.layout.content_files);

        new FilesPresenter(this, this);

        mSRLayout = (SwipeRefreshLayout) findViewById(R.id.files_SRLayout);
        mPathRV = (RecyclerView) findViewById(R.id.files_path_RV);
        mContentRV = (RecyclerView) findViewById(R.id.files_content_RV);
        mCodeWB = (WebView) findViewById(R.id.files_code_WB);
        mContentLayout = (LinearLayout) findViewById(R.id.files_file_content_layout);
        mFileLayout = (NestedScrollView) findViewById(R.id.files_file_NSV);
        mFileTV = (AppCompatTextView) findViewById(R.id.files_file_TV);

        setupRecyclerView();
        setupSwipeRefreshLayout();

        mSRLayout.setRefreshing(true);
        loadContents();
    }


    private void getParams() {
        repo = getIntent().getStringExtra(REPO);
        owner = getIntent().getStringExtra(OWNER);
        ref = getIntent().getStringExtra(REF);
        branch = getIntent().getStringExtra(BRANCH);
        url = getIntent().getStringExtra(URL);
        path = "";
    }

    @Override
    protected void setToolbar() {
        super.setToolbar();
        setToolbarTitle(repo);
        setOnToolbarNavClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void launchActivity(Context context, String owner, String repo,
                                      String ref, String branch, String url) {
        Intent intent = new Intent();
        intent.putExtra(REPO, repo);
        intent.putExtra(OWNER, owner);
        intent.putExtra(BRANCH, branch);
        intent.putExtra(REF, ref);
        intent.putExtra(URL, url);
        intent.setClass(context, FilesActivity.class);
        context.startActivity(intent);
//        ((Activity) context).finish();
    }

    private void setupSwipeRefreshLayout() {
        mSRLayout.setColorSchemeResources(R.color.colorAccent);
        mSRLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        mSRLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isFileLoading) {
                    loadContents();
                } else {
                    loadFile();
                }
            }
        });
    }

    private void setupRecyclerView() {
        mDirAdapter = new FileDirRecyclerViewAdapter(this);
        mPathAdapter = new PathRecyclerViewAdapter(this);

        mPathRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mPathRV.setAdapter(mPathAdapter);

        mContentRV.setLayoutManager(new LinearLayoutManager(this));
        mContentRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mContentRV.setAdapter(mDirAdapter);
        mPathAdapter.setOnItemClickListener(new OnDirItemClickListener() {
            @Override
            public void onClick(View v, String p, String type, String sha) {
                if (p.equals(path)) {
                    return;
                }
                mSRLayout.setEnabled(true);
                isFileLoading = false;
                mContentLayout.setVisibility(View.GONE);
                path = p;
                mSRLayout.setRefreshing(true);
                loadContents();
            }
        });
        mDirAdapter.setOnItemClickListener(new OnDirItemClickListener() {
            @Override
            public void onClick(View v, String p, String type, String sha) {
                FilesActivity.this.sha = sha;
                mSRLayout.setEnabled(true);
                String[] strs = p.split("/");
                if (p.equals("root system/" + path + "/" + strs[strs.length - 1])) {
                    return;
                }
                path = p;
                Log.i(TAG, p);
                if (type != null ) {
                    mSRLayout.setRefreshing(true);
                    if (type.equals("dir")) {
                        isFileLoading = false;
                        loadContents();
                    } else if (type.equals("file")) {
                        isFileLoading = true;
                        loadFile();
                        String[] s = p.split("/");
                        List<String> paths = new ArrayList<>();
                        for (int i = 0; i < s.length; i ++) {
                            paths.add(s[i]);
                        }
                        mPathAdapter.swapAllData(paths);
                        mContentRV.setVisibility(View.GONE);
                        Codeview.with(FilesActivity.this)
                                .withCode("")
                                .setStyle(Settings.WithStyle.GITHUB)
                                .setLang(Settings.Lang.JAVA)
                                .into(mCodeWB);
                        mFileTV.setText("");
                    } else {
                        isFileLoading = false;
                        mSRLayout.setRefreshing(false);
                    }
                }
            }
        });
        mFileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "textview clicked");
            }
        });
    }

    private void loadContents() {
        mPresenter.loadContent(owner, repo, path);
    }

    private void loadFile() {
        mPresenter.loadFile(owner, repo, path, sha);
    }

    @Override
    public void setPresenter(FilesContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void loadContentSuccess() {
        String[] strs = contentList.get(0).getPath().split("/");
            List<String> paths = new ArrayList<>();
            for (int i = 0; i < strs.length - 1; i ++) {
                paths.add(strs[i]);
            }
            mContentRV.setVisibility(View.VISIBLE);
            mPathAdapter.swapAllData(paths);
            mDirAdapter.swapAllData(contentList);
            mSRLayout.setRefreshing(false);
            mSRLayout.setEnabled(false);
    }

    @Override
    public void loadContentFail() {
        mSRLayout.setRefreshing(false);
    }

    @Override
    public void loadingContent(List<RepositoryContentBean> beanList) {
        contentList.clear();
        contentList.addAll(arrangeList(beanList));
    }

    @Override
    public void loadFileSuccess() {
        mSRLayout.setEnabled(false);
        mContentLayout.setVisibility(View.VISIBLE);
        setFileContent();
        mSRLayout.setRefreshing(false);
    }

    @Override
    public void loadFileFail() {
        mSRLayout.setRefreshing(false);
    }

    @Override
    public void loadingFile(String file) {
        fileContent = file + "\n" + "\n" + "\n" + "\n";
    }

    @Override
    public String getRef() {
        return ref;
    }

    @Override
    public String getBranch() {
        return branch;
    }

    private List<RepositoryContentBean> arrangeList(List<RepositoryContentBean> list) {
        List<RepositoryContentBean> dirs = new ArrayList<>();
        List<RepositoryContentBean> files = new ArrayList<>();
        List<RepositoryContentBean> symlinks = new ArrayList<>();
        for (RepositoryContentBean model : list) {
            if (model.getType().equals("dir")) {
                dirs.add(model);
            } else if (model.getType().equals("file")) {
                files.add(model);
            } else if (model.getType().equals("symlinks")) {
                symlinks.add(model);
            }
        }
        List<RepositoryContentBean> newList = new ArrayList<>();
        newList.addAll(dirs);
        newList.addAll(files);
        newList.addAll(symlinks);
        return newList;
    }

    private void setFileContent() {
        String language = null;
        if (path.endsWith(".java")) {
            language = Settings.Lang.JAVA;
        } else if (path.endsWith(".c") || path.endsWith(".cpp")) {
            language = Settings.Lang.CPLUSPLUS;
        } else if (path.endsWith(".cs")) {
            language = Settings.Lang.CSHARP;
        } else if (path.endsWith(".js")) {
            language = Settings.Lang.JAVASCRIPT;
        } else if (path.endsWith(".py")) {
            language = Settings.Lang.PYTHON;
        } else if (path.endsWith(".rb")) {
            language = Settings.Lang.RUBY;
        } else if (path.endsWith(".php")) {
            language = Settings.Lang.PHP;
        }  else if (path.endsWith(".jpg") || path.endsWith(".JPG")
                || path.endsWith(".GIF") || path.endsWith(".gif")
                || path.endsWith(".png") || path.endsWith(".PNG")){
            setImageOrFile(true);
            return;
        } else if (path.endsWith(".md")){
            setImageOrFile(false);
            return;
        } else if (path.endsWith(".xml")) {
            fileContent = fileContent.replaceAll("<", "&lt;");
            fileContent = fileContent.replaceAll(">", "&gt;");
            fileContent = "<div class=\"plain\"><pre>" + fileContent + "</pre></div>";
        } else {
            language = Settings.MimeType.TEXT_HTML;
        }
        setCode(language);
    }

    private void setImageOrFile(boolean isImage) {
        if (isImage) {
            mCodeWB.setVisibility(View.VISIBLE);
            mFileLayout.setVisibility(View.GONE);
            mCodeWB.loadDataWithBaseURL(null,
                    "<html><head></head><body><table style=\"width:100%; height:100%;\"><tr><td style=\"vertical-align:middle;\"><img src=\""
                            + url + "/raw/" + ref + "/" + path + "\"></td></tr></table></body></html>",
                    "html/css",
                    "utf-8",
                    null);
        } else {
            mCodeWB.setVisibility(View.GONE);
            mFileLayout.setVisibility(View.VISIBLE);
            HtmlImageGetter imageGetter = new HtmlImageGetter(mFileTV, this,
                    url + "/raw/" + ref);
            TextUtil.showReadMe(mFileTV, fileContent, imageGetter);
        }
    }

    private void setCode(String lang) {
        mCodeWB.setVisibility(View.VISIBLE);
        mFileLayout.setVisibility(View.GONE);
        Log.i(TAG, fileContent);
        Codeview.with(this)
                .withCode(fileContent)
                .setStyle(Settings.WithStyle.GITHUB)
                .setLang(lang)
                .into(mCodeWB);
    }
}
