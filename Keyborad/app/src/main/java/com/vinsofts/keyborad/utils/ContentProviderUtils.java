package com.vinsofts.keyborad.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ha on 5/4/2017.
 */

public class ContentProviderUtils {
    public static class Contacts {
        private String id;
        private String name;
        private String image;
        private List<String> number;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getNumber() {
            return number;
        }

        public void setNumber(List<String> number) {
            this.number = number;
        }
    }

    public static ArrayList<Contacts> getListContacts(Context context) {
        ArrayList<Contacts> listContacts = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur != null && cur.getCount() > 0) {
            int indexId = cur.getColumnIndex(ContactsContract.Contacts._ID);
            int indexName = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int indexImage = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
            while (cur.moveToNext()) {
                String id = cur.getString(indexId);
                String name = cur.getString(indexName);
                String image = cur.getString(indexImage);
                Contacts contacts = new Contacts();
                contacts.setId(id);
                contacts.setName(name);
                if (image != null) {
                    contacts.setImage(image);
                }
                List<String> list = new ArrayList<>();
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            list.add(phoneNo);
                        }
                        pCur.close();
                    }
                }
                contacts.setNumber(list);
                listContacts.add(contacts);
            }

            cur.close();
        }
        Collections.sort(listContacts, new Comparator<Contacts>() {
            @Override
            public int compare(Contacts obj1, Contacts obj2) {
                return obj1.getName().compareTo(obj2.getName());
            }
        });
        return listContacts;
    }

    public static ArrayList<String> getImages(Context context) {
        ArrayList<String> listImage = new ArrayList<String>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int indexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            int indexFolderName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                String pathImage = cursor.getString(indexData);
                listImage.add(pathImage);
            }
            cursor.close();
        }
        return listImage;
    }
//
//    public static List<Music> getAllSongs(Context context) {
//
//        Uri allSongUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
//        Cursor cursor = context.getContentResolver().query(allSongUri, null, null, null, selection);
//
//        List<Music> list = new ArrayList<>();
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                int indexSongName = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//                int indexId = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
//                int indexPath = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
//                int indexDuration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
//                int indexAlbumId = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID);
//                int indexAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
//                int indexArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//                int indexSize = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
//                int indexTime = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);
//
//                do {
//                    int id = cursor.getInt(indexId);
//                    String songName = cursor.getString(indexSongName);
//                    String fullPath = cursor.getString(indexPath);
//                    long duration = cursor.getLong(indexDuration);
//                    long albumId = cursor.getLong(indexAlbumId);
//                    String album = cursor.getString(indexAlbum);
//                    String artist = cursor.getString(indexArtist);
//                    long size = cursor.getLong(indexSize);
//                    long timeCreate = cursor.getLong(indexTime);
//
//
//                    Music music = new Music();
//                    music.setId(String.valueOf(id));
//                    music.setAvatarAlbum(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId));
//                    music.setName(songName);
//                    music.setPath(fullPath);
//                    music.setArtist(artist);
//                    music.setAlbum(album);
//                    music.setSize(size);
//                    music.setDuration(duration);
//                    music.setTimeCreate(timeCreate);
//                    list.add(music);
//
////                    MLog.e(DatabaseUtils.dumpCursorToString(cursor));
//                } while (cursor.moveToNext());
//
//            }
//            cursor.close();
//
//        }
//        Collections.sort(list, (obj1, obj2) -> obj1.getName().compareTo(obj2.getName()));
//        MLog.e(list);
//        return list;
//    }

    //
//    public static List<Music> getAllCutter(Context context) {
//        File folder = new File(AppUtils.getFolderSaveCutter());
//
//        File[] files = folder.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File file) {
//                return file.isFile();
//            }
//        });
//        List<Music> list = new ArrayList<>();
//        for (int i = 0; i < files.length; i++) {
//            Music music = new Music();
//            music.setId("0");
//            music.setAvatarAlbum(Uri.parse(""));
//            music.setName(files[i].getName());
//            music.setPath(files[i].getPath());
//            music.setArtist("VinSoft Mp3Cutter");
//            music.setAlbum("");
//            music.setSize(files[i].length());
//            music.setDuration(10000);
//            music.setTimeCreate(Calendar.getInstance().getTimeInMillis());
//
//            list.add(music);
//        }
//
//        return list;
//    }


//    public static List<Video> getAllRecord(Context context) {
//        String selection = MediaStore.Video.Media.DATA + " like ?";
//        String[] selectionArgs = new String[]{"%MyRecordScreen/Video%"};
//        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                null, selection, selectionArgs, null);
//        List<Video> list = new ArrayList<>();
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                int indexName = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
//                int indexId = cursor.getColumnIndex(MediaStore.Video.Media._ID);
//                int indexPath = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
//                int indexDuration = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION);
//                int indexRESOLUTION = cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION);
//                int indexSize = cursor.getColumnIndex(MediaStore.Video.Media.SIZE);
//                int indexTime = cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED);
//
//                do {
//                    long id = cursor.getLong(indexId);
//                    String name = cursor.getString(indexName);
//                    String path = cursor.getString(indexPath);
//                    long duration = cursor.getLong(indexDuration);
//                    long size = cursor.getLong(indexSize);
//                    long timeCreate = cursor.getLong(indexTime);
//                    String RESOLUTION = cursor.getString(indexRESOLUTION);
//
//                    Video video = new Video();
//                    video.setId(id);
//                    video.setName(name);
//                    video.setPath(path);
//                    video.setSize(size);
//                    video.setDuration(duration);
//                    video.setDateCreate(timeCreate);
//                    video.setResoulution(RESOLUTION);
//
//                    File file = new File(path);
//                    if (file.length() > 100) {
//                        video.setSize(file.length());
//                        list.add(video);
//                    }
////                    MLog.e(DatabaseUtils.dumpCursorToString(cursor));
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        }
//        return list;
//    }
//
//    public static List<Video> getAllCapture(Context context) {
//        File folder = new File(AppUtils.getFolderSaveCapture());
//        File[] files = folder.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File file) {
//                return file.isFile();
//            }
//        });
//        List<Video> list = new ArrayList<>();
//        for (int i = 0; i < files.length; i++) {
//            Video music = new Video();
//            music.setId(1);
//            music.setName(files[i].getName());
//            music.setPath(files[i].getPath());
//            music.setSize(files[i].length());
//            music.setDuration(10000);
//
//            list.add(music);
//        }
//        return list;
//    }

    static class Music {
        private String id;
        private String name;
        private String artist;
        private String album;
        private Uri avatarAlbum;
        private String path;
        private long size;
        private long timeCreate;
        private long duration;

        public long getTimeCreate() {
            return timeCreate;
        }

        public void setTimeCreate(long timeCreate) {
            this.timeCreate = timeCreate;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public Uri getAvatarAlbum() {
            return avatarAlbum;
        }

        public void setAvatarAlbum(Uri avatarAlbum) {
            this.avatarAlbum = avatarAlbum;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }


    }

}
