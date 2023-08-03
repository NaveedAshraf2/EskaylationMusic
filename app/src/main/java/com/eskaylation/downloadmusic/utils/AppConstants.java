package com.eskaylation.downloadmusic.utils;

import android.content.Context;
import android.graphics.Color;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.model.BackgroundModel;
import com.eskaylation.downloadmusic.model.TabModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppConstants {
    public static int randomThumb() {
        return R.drawable.ic_player_default;
    }

    public static ArrayList<String> listPubBlock() {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            JSONArray jSONArray = new JSONObject("{\"list\":[\"T-SERIES\",\"ZEE MUSIC COMPANY\",\"SHEMAROO FILMI GAANE\",\"WAVE MUSIC\",\"SONY MUSIC INDIA\",\"YRF\",\"SPEED RECORDS\",\"T-SERIES BHAKTI SAGAR\",\"TIPS OFFICIAL\",\"EROS NOW\",\"T-SERIES APNA PUNJAB\",\"ULTRA BOLLYWOOD\",\"VENUS\",\"GAANE SUNE ANSUNE\",\"GEET MP3\",\"RAJSHRI\",\"BOLLYWOOD CLASSICS\",\"DESI MUSIC FACTORY\",\"SONOTEK\",\"WAVE MUSIC - BHOJPURI\",\"WHITE HILL MUSIC\",\"WORLDWIDE RECORDS BHOJPURI\",\"SAREGAMA MUSIC\",\"ADITYA MUSIC\",\"EMIWAY BANTAI\",\"POP CHARTBUSTERS\",\"TEAM FILMS BHOJPURI\",\"JIGAR MUSIC\",\"LOKDHUN PUNJABI\",\"NEHA KAKKAR\",\"HARYANVI MAINA\",\"JASS RECORDS\",\"LAHARI MUSIC | T-SERIES\",\"SONOTEK HARYANVI\",\"T-SERIES REGIONAL\",\"BOLLYWOOD CLASSICS\",\"T-SERIES HAMAAR BHOJPURI\",\"WAVE MUSIC - BHAKTI\",\"MOR HARYANVI\",\"SONOTEK BHAKTI\",\"BHAKTI DHARA\",\"SANAM\",\"THINK MUSIC INDIA\",\"RDC GUJARATI\",\"SATYAJEET JENA OFFICIAL\",\"VENUS BHOJPURI\",\"RCM BHOJPURI HIT\",\"SONOTEK MUSIC\",\"SONOTEK PUNJABI\",\"TIMES MUSIC\",\"SAGAHITS\",\"CHETAK\",\"RDC RAJASTHANI\",\"TRIMURTI CASSETTES\",\"AADISHAKTI FILMS\",\"VENUS REGIONAL\",\"ANAND AUDIO\",\"AMBEY BHAKTI\",\"SRK MUSIC\",\"MAD 4 MUSIC\",\"SHREE JEE - BHAKTI\",\"STUDIO SARASWATI OFFICIAL\",\"HUMBLE MUSIC\",\"CHANDA\",\"ULTRA CINEMA\",\"DM - DESI MELODIES\",\"SHEMAROO BHAKTI\",\"PEHCHAN MUSIC\",\"SUPERTONE DIGITAL\",\"NDJ MUSIC\",\"HD SONGS BOLLYWOOD\",\"SANJIVANI BHOJPURI\",\"VOICE OF HEART MUSIC\",\"RAGHAV DIGITAL\",\"RAJASTHANI GORBAND MUSIC HD\",\"FRESH MEDIA RECORDS\",\"AWADHESH PREMI OFFICIAL\",\"SPIRITUAL MANTRA\",\"GANGA BHAKTI\",\"WUNDERBAR STUDIOS\",\"MANGO MUSIC\",\"CROWN RECORDS\",\"SAI RECORDDS DIGITAL\",\"T-SERIES TELUGU\",\"ENTERR10 MUSIC BHOJPURI\",\"GULZAAR CHHANIWALA\",\"SHEMAROO KIDS\",\"ANGLE MUSIC OFFICIAL CHANNEL\",\"SINGLE TRACK STUDIO\",\"MELVIN LOUIS\",\"BHAJAN INDIA\",\"ZEE MUSIC MARATHI\",\"SIDHU MOOSE WALA\",\"BIHARIWOOD - बिहारीवुड\",\"RDC RAJASTHANI HD\",\"KHESARI MUSIC WORLD\",\"GAJENDRA VERMA\",\"DINO JAMES\",\"MUZIK247\",\"JUKE DOCK\",\"NDJ FILM OFFICIAL\",\"RAFTAAR\",\"SONOTEK DIGITAL\",\"GURU RANDHAWA\",\"ZEE MUSIC SOUTH\",\"UNKNOWN BOY VARUN\",\"VYRLORIGINALS\",\"SAREGAMA TAMIL\",\"RAJSHRI TAMIL\",\"EKTA SOUND\",\"BEINGINDIAN\",\"SAREGAMA BENGALI\",\"MB MUSIC\",\"MEDITATIVE MIND\",\"RIYA FILMS\",\"TARANG MUSIC\",\"TOHAAR BHOJPURI - तोहार भोजपुरी\",\"JAYAS KUMAR\",\"RAM AUDIO\",\"SONIC ENTERPRISE\",\"SAREGAMA GENY\",\"BAMB BEATS\",\"INDIE MUSIC LABEL\",\"VEHLIJANTARECORDS\",\"SONIC ISLAMIC (سونک اسلامک)\",\"KANHAIYACASSETTE\",\"RAJASTHANI GORBAND MUSIC\",\"YO YO HONEY SINGH\",\"WINGS MARATHI\",\"STUDIO SANGEETA\",\"RDC BANGLAR GEETI\",\"DIL MUSIC\",\"MESHWA FILMS\",\"SHEMAROO MUSICAL MAESTROS\",\"KNOX ARTISTE\",\"CHEETA SUPERFINE MUSIC\",\"DANISH & DAWAR\",\"AIDC\",\"LYCA PRODUCTIONS\",\"RAJ BARMAN\",\"T-SERIES ISLAMIC MUSIC\",\"PRG MUSIC AND FILM STUDIO\",\"THE UNFORGETTABLE ENTERTAINMENT\",\"AKSH BAGHLA\",\"DARSHANRAVALDZ\",\"HSR ENTERTAINMENT\",\"PRIYA VIDEO\",\"SURANA FILM STUDIO\",\"VEENA MUSIC\",\"BHAJAN KIRTAN\",\"MALWA RECORDS\",\"YUKI MUSIC\",\"GRAND STUDIO\",\"TARANG MUSIC\",\"RIDDHI MUSIC WORLD\",\"YFILMS\",\"SOORMANDIR\",\"DIVINE\",\"FINE DIGITAL HARYANVI\",\"BHAKTI BHAJAN KIRTAN\",\"RCM BHAKTI SAGAR\",\"JIGAR STUDIO\",\"JHANKAR MUSIC\",\"TEAM FILM\",\"SAI AASHIRWAD\",\"A. R. RAHMAN\",\"KARAMBIR FOUJI LIVE\",\"BANGLAR RS MUSIC\",\"NAV HARYANVI\",\"BILLIONSURPRISETOYS - TAMIL\",\"PANJ-AAB RECORDS\",\"JAMMA DESI\",\"BHAKTI MALA\",\"TADKA PUNJABI\",\"ALKA MUSIC\",\"BHAKTI DHAAM\",\"DEVOTEES INSANOS RECORDS\",\"BOLLYWOOD ROMANTIC JUKEBOX\",\"AWANTIKA MUSIC\",\"RITU AGARWAL\",\"SURINDER FILMS\",\"SHREE CASSETTE ISLAMIC\",\"BANG MUSIC\",\"PRAMOD PREMI ENTERTAINMENT\",\"RHYTHM BOYZ\",\"HIPHOP TAMIZHA\",\"DISCO RECORDING COMPANY - TELANGANA FOLKS\",\"J STAR PRODUCTIONS\",\"SHABAD GURBANI\",\"LALITHA AUDIOS AND VIDEOS\",\"I AM DESI WORLD\",\"SIDDHARTH SLATHIA\",\"UNITED WHITE FLAG\",\"SONU KAKKAR\",\"MK STUDIO\",\"AGR LIFE\",\"ARMAAN MALIK\",\"ROYAL MUSIC FACTORY\",\"BRIJWANI CASSETTES\",\"WAVE DHAMAKA\",\"SHIVANI KA THUMKA\",\"SUNIX THAKOR\",\"SPEED PUNJABI\",\"8D SONGS BOLLYWOOD\",\"AARYAA DIGITAL\",\"AISH\",\"SAAWARIYA\",\"VENUS DEVOTIONAL\",\"PK DJ STUDIO\",\"HARYANVI HITS\",\"HARI OM BHAKTI\",\"JAI SHRI RAM BHAKTI\",\"MANKIRT AULAKH\",\"BABBU MAAN\",\"LOVELY VIDEO\",\"MAITHILI THAKUR\",\"SPEED RECORDS BHOJPURI\",\"VIBE MUSIC\",\"P&M SAPNA OFFICIAL\",\"LATEST PUNJABI SONGS\",\"RUHAAN ARSHAD OFFICIAL\",\"ONE MUSIC ORIGINALS\",\"EVEREST MARATHI\",\"RDC GUJARATI HD\",\"DESI UNPLUGGED\",\"LOVEINDIA\",\"YUMNA AJIN OFFICIAL\",\"RAJEEV RAJA\",\"STRUMM SOUND\",\"HINDI UNPLUGGED WORLD\",\"HANSRAJ RAGHUWANSHI\",\"BIHARI FILMS BHOJPURI\",\"VIDEO PALACE\",\"SAGA MUSIC\",\"MG RECORDS HARYANVI HITS\",\"KALA NIKETAN\",\"JHANKAR GAANE\",\"RAJSHRI SOUL\",\"GURJAR RASIYA OFFICIAL\",\"BENGALI SONGS - ANGEL DIGITAL\",\"TIMES MUSIC SOUTH\",\"EMM PEE\",\"SONIC QAWWALI\",\"SAI RECORDDS - BHOJPURI\",\"KULDEEP M PAI\",\"AWADHESH PREMI PERSONAL\",\"HEMANTKUMAR MAHALE\",\"SATYAMVIDEOS\",\"T-SERIES TAMIL\",\"DHVANI BHANUSHALI\"]}").getJSONArray("list");
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jSONArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static final List<String> testDevices() {
        return Arrays.asList(new String[]{"5A8167409A32C7D3AB959286717678D3",
                "C6647307A1AA632732F9986A242BCC85", "A7CEFE0B9B06F4A3DCF576CBB4BC9DF5",
                "F1E3830B5B08242AE9D240EA7CE8F388", "55ECB36F8A4DE2D4BD04AF5B9C6171B2",
                "1898043E9C40FB409D52C3DF62F075D4", "F51A24B480339E82777846945F2FDDDD"});
    }

    public static ArrayList<TabModel> getListTab(Context context) {
        ArrayList<TabModel> arrayList = new ArrayList<>();
        arrayList.add(new TabModel(context.getString(R.string.txt_search), R.drawable.ic_search_shape, R.drawable.cardviewww));
        arrayList.add(new TabModel(context.getString(R.string.tit_song), R.drawable.ic_music_shape, R.drawable.cardviewww));
        arrayList.add(new TabModel(context.getString(R.string.tit_album), R.drawable.ic_album_shape, R.drawable.cardviewww));
        arrayList.add(new TabModel(context.getString(R.string.tit_artist), R.drawable.ic_artist_shape, R.drawable.cardviewww));
       // arrayList.add(new TabModel(context.getString(R.string.tit_folder), R.drawable.ic_folder_shape, R.drawable.shape_5));
        arrayList.add(new TabModel(context.getString(R.string.tit_playlist), R.drawable.ic_playlist_shape, R.drawable.cardviewww));
        return arrayList;
    }

    public static ArrayList<BackgroundModel> listBackground(Context context) {
        ArrayList<BackgroundModel> arrayList = new ArrayList<>();
        //arrayList.add(new BackgroundModel(R.drawable.bg_1, R.drawable.bg_navigation1, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        arrayList.add(new BackgroundModel(R.drawable.ads, R.drawable.ads, -1, -1));
        String str = "#030744";
        arrayList.add(new BackgroundModel(-1, -1, Color.parseColor(str), Color.parseColor("#197cb3")));
        arrayList.add(new BackgroundModel(-1, -1, Color.parseColor("#300015"), Color.parseColor("#d24043")));
        arrayList.add(new BackgroundModel(-1, -1, Color.parseColor("#003d6e"), Color.parseColor("#18e4fd")));
        arrayList.add(new BackgroundModel(-1, -1, Color.parseColor("#214a44"), Color.parseColor("#2ed65c")));
        arrayList.add(new BackgroundModel(-1, -1, Color.parseColor(str), Color.parseColor("#3ff4bd")));
        arrayList.add(new BackgroundModel(-1, -1, Color.parseColor("#01003e"), Color.parseColor("#cf4497")));
        arrayList.add(new BackgroundModel(-1, -1, Color.parseColor("#110449"), Color.parseColor("#ff1f1b")));
        arrayList.add(new BackgroundModel(-1, -1, Color.parseColor("#2a0429"), Color.parseColor("#b42d67")));
        arrayList.add(new BackgroundModel(-1, -1, Color.parseColor("#1F1C18"), Color.parseColor("#8E0E00")));
        arrayList.add(new BackgroundModel(-1, -1, Color.parseColor("#093637"), Color.parseColor("#44A08D")));
        return arrayList;
    }
}
