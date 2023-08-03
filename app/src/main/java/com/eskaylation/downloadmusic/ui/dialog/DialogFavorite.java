package com.eskaylation.downloadmusic.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.adapter.AddPlaylistSongAdapter;
import com.eskaylation.downloadmusic.bus.AddToLoveSong;
import com.eskaylation.downloadmusic.bus.CreatePlaylist;
import com.eskaylation.downloadmusic.database.FavoriteDao;
import com.eskaylation.downloadmusic.database.FavoriteSqliteHelper;
import com.eskaylation.downloadmusic.database.SongListDao;
import com.eskaylation.downloadmusic.database.SongListSqliteHelper;
import com.eskaylation.downloadmusic.listener.OnAddFavoriteClickListener;
import com.eskaylation.downloadmusic.model.Favorite;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.utils.ToastUtils;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;

public class DialogFavorite {
    public Context context;
    public FavoriteDao favoriteDao ;
    public DialogFavoriteListener listener;
    public Song song;
    public SongListDao songListDao;
    public SongListSqliteHelper songListSqliteHelper;
    public FavoriteSqliteHelper sqliteHelper;

    public DialogFavorite(Context context2, DialogFavoriteListener dialogFavoriteListener) {
        this.listener = dialogFavoriteListener;
        this.context = context2;
        this.sqliteHelper = new FavoriteSqliteHelper(context2);
        favoriteDao = new FavoriteDao(this.context, this.sqliteHelper);
    }

    public void showDialogCreateFavorite(int i, Favorite favorite) {
        Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_favorite);
        Window window = dialog.getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        attributes.flags &= -5;
        window.setAttributes(attributes);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.getWindow().setLayout(-1, -2);
        EditText editText = (EditText) dialog.findViewById(R.id.edt_name);
        editText.requestFocus();
        TextView textView = (TextView) dialog.findViewById(R.id.tv_create);
        TextView textView2 = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView textView3 = (TextView) dialog.findViewById(R.id.title_playlist);
        if (favorite != null) {
            editText.setText(favorite.name);
            textView.setText(this.context.getString(R.string.update));
            textView3.setText(this.context.getString(R.string.update_playlist));
        }

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFavorite.this.DialogFavorite0(editText, favorite, i, dialog, view);
            }
        });
        textView2.setOnClickListener(new OnClickListener() {
      

            public final void onClick(View view) {
                DialogFavorite.this.DialogFavorite1(editText, dialog, view);
            }
        });
        dialog.getWindow().setSoftInputMode(4);
        dialog.show();
    }

    public  void DialogFavorite0(EditText editText, Favorite favorite, int i, Dialog dialog, View view) {
        if (editText.getText().toString().trim().isEmpty()) {
            ToastUtils.warning(this.context, (int) R.string.please_enter_playlist);
        } else if (favorite != null) {
            String obj = editText.getText().toString();
            String str = favorite.name;
            favorite.setName(obj);
            if (this.favoriteDao.getAllNameFavorite().contains(obj) || this.favoriteDao.getAllFavoriteID().contains(obj)) {
                ToastUtils.error(this.context, (int) R.string.please_chose_other_name);
            } else if (this.favoriteDao.updateFavoriteName(str, obj) != 1) {
                ToastUtils.error(this.context, (int) R.string.cant_not_update);
            } else {
                this.listener.onUpdatePlaylist(i, favorite);
                hideKeyboard(editText);
                dialog.dismiss();
            }
        } else {
            String obj2 = editText.getText().toString();
            StringBuilder sb = new StringBuilder();
            sb.append("MP3_");
            sb.append(PreferenceUtils.getInstance(this.context).getIDPlaylist());
            String sb2 = sb.toString();
            if (this.favoriteDao.getAllNameFavorite().contains(obj2) || this.favoriteDao.getAllFavoriteID().contains(obj2)) {
                ToastUtils.error(this.context, (int) R.string.please_chose_other_name);
            } else if (this.favoriteDao.insertFavorite(obj2) != -1) {
                if (i == -1) {
                    this.listener.onCreatePlaylistFromDialog(this.song);
                } else {
                    this.listener.onCreateNewPlaylist(new Favorite(0, sb2, obj2));
                }
                EventBus.getDefault().postSticky(new CreatePlaylist("CREATE"));
                hideKeyboard(editText);
                dialog.dismiss();
            } else {
                ToastUtils.warning(this.context, (int) R.string.txt_new_playlist_exist);
            }
        }
    }

    public  void DialogFavorite1(EditText editText, Dialog dialog, View view) {
        hideKeyboard(editText);
        dialog.dismiss();
    }

    public void showDialogMore(int i, Favorite favorite) {
        Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_more_favorite);
        Window window = dialog.getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        attributes.flags &= -5;
        window.setAttributes(attributes);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.getWindow().setLayout(-1, -2);
        TextView textView = (TextView) dialog.findViewById(R.id.btn_rename);
        TextView textView2 = (TextView) dialog.findViewById(R.id.btn_delete);
        TextView textView3 = (TextView) dialog.findViewById(R.id.btnAddSong);

//        ((TextView) dialog.findViewById(R.id.tv_name)).setText(favorite.name);
        textView2.setOnClickListener(new OnClickListener() {

            public final void onClick(View view) {
                DialogFavorite.this.DialogFavorite2(favorite, i, dialog, view);
            }
        });
        textView.setOnClickListener(new OnClickListener() {


            public final void onClick(View view) {
                DialogFavorite.this.DialogFavorite3(i, favorite, dialog, view);
            }
        });
        textView3.setOnClickListener(new OnClickListener() {
        
            public final void onClick(View view) {
                DialogFavorite.this.DialogFavorite4(favorite, dialog, view);
            }
        });
        dialog.show();
    }

    public  void DialogFavorite2(Favorite favorite, int i, Dialog dialog, View view) {
        if (this.favoriteDao.deleteFavorite(favorite.name) == 1) {
            this.songListSqliteHelper = new SongListSqliteHelper(this.context, favorite.favorite_id);
            this.songListDao = new SongListDao(this.songListSqliteHelper);
            this.songListDao.deleteFavoriteDB(favorite.favorite_id);
            ToastUtils.success(this.context, (int) R.string.delete_playlist);
            this.listener.deletePlaylistDone(i);
            dialog.dismiss();
            return;
        }
        ToastUtils.error(this.context, (int) R.string.cant_delete_playlist);
        dialog.dismiss();
    }

    public  void DialogFavorite3(int i, Favorite favorite, Dialog dialog, View view) {
        showDialogCreateFavorite(i, favorite);
        dialog.dismiss();
    }

    public  void DialogFavorite4(Favorite favorite, Dialog dialog, View view) {
        this.listener.onOpenAddSong(favorite);
        dialog.dismiss();
    }

    public void showDialogAddSong(Song song2, boolean z) {
        this.song = song2;
        ArrayList allFavorite = this.favoriteDao.getAllFavorite();
        Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_add_playlist_song);
        Window window = dialog.getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        attributes.flags &= -5;
        window.setAttributes(attributes);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.getWindow().setLayout(-1, -2);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.rv_playlist);
        ((ImageButton) dialog.findViewById(R.id.create_playlist)).setOnClickListener(new OnClickListener() {


            public final void onClick(View view) {
                DialogFavorite.this.DialogFavorite5(dialog, view);
            }
        });
        Context context2 = this.context;
        final ArrayList arrayList = allFavorite;
        final Song song3 = song2;
        final Dialog dialog2 = dialog;
        final boolean z2 = z;

        AddPlaylistSongAdapter addPlaylistSongAdapter = new AddPlaylistSongAdapter(context2, allFavorite, new OnAddFavoriteClickListener() {
            @Override
            public void onItemFavoriteClick(int i) {
                if (new SongListDao(new SongListSqliteHelper(DialogFavorite.this.context, ((Favorite) arrayList.get(i)).favorite_id)).insertFavoriteSong(song3) != -1) {
                    ToastUtils.success(DialogFavorite.this.context, (int) R.string.done);
                } else {
                    ToastUtils.error(DialogFavorite.this.context, (int) R.string.song_exist);
                }
                dialog2.dismiss();
                if (i == 0) {
                    EventBus.getDefault().postSticky(new AddToLoveSong(z2));
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(addPlaylistSongAdapter);
        dialog.show();
    }

    public  void DialogFavorite5(Dialog dialog, View view) {
        showDialogCreateFavorite(-1, null);
        dialog.dismiss();
    }

    public void hideKeyboard(EditText editText) {
        ((InputMethodManager) this.context.getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
