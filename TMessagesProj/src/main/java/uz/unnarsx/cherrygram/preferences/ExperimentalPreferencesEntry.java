/**
 * This is the source code of Cherrygram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package uz.unnarsx.cherrygram.preferences;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.view.View;

import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;

import uz.unnarsx.cherrygram.core.crashlytics.FirebaseAnalyticsHelper;
import uz.unnarsx.cherrygram.core.configs.CherrygramExperimentalConfig;
import uz.unnarsx.cherrygram.helpers.ui.PopupHelper;
import uz.unnarsx.cherrygram.preferences.helpers.SettingsHelper;

public class ExperimentalPreferencesEntry extends UniversalFragment {

    private static final int STREAM_MKV = 1, STREAM_ALL_VIDEO = 2, MP4_PREVIEW = 3,
            HLS_QUALITY = 4, VIDEO_DECODER = 5, ENHANCED_BITRATE = 6,
            SMALL_GIF = 7, COPY_VIDEO_FRAME = 8;

    @Override
    protected CharSequence getTitle() {
        FirebaseAnalyticsHelper.INSTANCE.trackEventWithEmptyBundle("experimental_preferences_screen");
        return getString(R.string.EP_Category_Experimental);
    }

    @Override
    public View createView(Context context) {
        setMD3(true);
        return super.createView(context);
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        items.add(UItem.asHeader(getString(R.string.CG_MediaPlayback)));
        items.add(SettingsHelper.asSwitchCG(STREAM_MKV, getString(R.string.CG_StreamMkv), getString(R.string.CG_StreamMkv_Desc)).setChecked(SharedConfig.streamMkv));
        items.add(SettingsHelper.asSwitchCG(STREAM_ALL_VIDEO, getString(R.string.CG_StreamAllVideo), getString(R.string.CG_StreamAllVideo_Desc)).setChecked(SharedConfig.streamAllVideo));
        items.add(SettingsHelper.asSwitchCG(MP4_PREVIEW, getString(R.string.CG_SendMp4DocumentAsVideo), getString(R.string.CG_SendMp4DocumentAsVideo_Desc)).setChecked(CherrygramExperimentalConfig.INSTANCE.getSendMp4DocumentAsVideo()));
        items.add(UItem.asButton(HLS_QUALITY, getString(R.string.CG_DefaultHlsVideoQuality), getHlsQualityValue()));
        items.add(UItem.asButton(VIDEO_DECODER, getString(R.string.CG_VideoPlayerDecoder), getVideoDecoderValue()));
        items.add(SettingsHelper.asSwitchCG(ENHANCED_BITRATE, getString(R.string.CG_EnhancedVideoBitrate), getString(R.string.CG_EnhancedVideoBitrate_Desc)).setChecked(CherrygramExperimentalConfig.INSTANCE.getEnhancedVideoBitrate()));
        items.add(SettingsHelper.asSwitchCG(SMALL_GIF, getString(R.string.CG_ShowSmallGif), getString(R.string.CG_ShowSmallGif_Desc)).setChecked(CherrygramExperimentalConfig.INSTANCE.getShowSmallGif()));
        items.add(SettingsHelper.asSwitchCG(COPY_VIDEO_FRAME, getString(R.string.CG_CopyVideoFrame), getString(R.string.CG_CopyVideoFrame_Desc)).setChecked(CherrygramExperimentalConfig.INSTANCE.getShowCopyVideoFrame()));
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == STREAM_MKV) { SharedConfig.toggleStreamMkv(); SettingsHelper.updateCheckState(view, SharedConfig.streamMkv); }
        else if (item.id == STREAM_ALL_VIDEO) { SharedConfig.toggleStreamAllVideo(); SettingsHelper.updateCheckState(view, SharedConfig.streamAllVideo); }
        else if (item.id == MP4_PREVIEW) { CherrygramExperimentalConfig.INSTANCE.setSendMp4DocumentAsVideo(!CherrygramExperimentalConfig.INSTANCE.getSendMp4DocumentAsVideo()); SettingsHelper.updateCheckState(view, CherrygramExperimentalConfig.INSTANCE.getSendMp4DocumentAsVideo()); }
        else if (item.id == HLS_QUALITY) showHlsQualitySelector(() -> SettingsHelper.updateButtonValue(view, getHlsQualityValue()));
        else if (item.id == VIDEO_DECODER) showVideoDecoderSelector(() -> SettingsHelper.updateButtonValue(view, getVideoDecoderValue()));
        else if (item.id == ENHANCED_BITRATE) { CherrygramExperimentalConfig.INSTANCE.setEnhancedVideoBitrate(!CherrygramExperimentalConfig.INSTANCE.getEnhancedVideoBitrate()); SettingsHelper.updateCheckState(view, CherrygramExperimentalConfig.INSTANCE.getEnhancedVideoBitrate()); }
        else if (item.id == SMALL_GIF) { CherrygramExperimentalConfig.INSTANCE.setShowSmallGif(!CherrygramExperimentalConfig.INSTANCE.getShowSmallGif()); SettingsHelper.updateCheckState(view, CherrygramExperimentalConfig.INSTANCE.getShowSmallGif()); }
        else if (item.id == COPY_VIDEO_FRAME) { CherrygramExperimentalConfig.INSTANCE.setShowCopyVideoFrame(!CherrygramExperimentalConfig.INSTANCE.getShowCopyVideoFrame()); SettingsHelper.updateCheckState(view, CherrygramExperimentalConfig.INSTANCE.getShowCopyVideoFrame()); }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }

    private String getHlsQualityValue() {
        return switch (CherrygramExperimentalConfig.INSTANCE.getDefaultHlsVideoQuality()) {
            case CherrygramExperimentalConfig.HLS_QUALITY_ORIGINAL -> getString(R.string.CG_QualityOriginal);
            case CherrygramExperimentalConfig.HLS_QUALITY_1440 -> "1440p";
            case CherrygramExperimentalConfig.HLS_QUALITY_1080 -> "1080p";
            case CherrygramExperimentalConfig.HLS_QUALITY_720 -> "720p";
            case CherrygramExperimentalConfig.HLS_QUALITY_LOW -> getString(R.string.CG_QualityLow);
            default -> "自动";
        };
    }

    private void showHlsQualitySelector(Runnable after) {
        ArrayList<String> labels = new ArrayList<>(); ArrayList<Integer> values = new ArrayList<>();
        labels.add("自动"); values.add(CherrygramExperimentalConfig.HLS_QUALITY_AUTO);
        labels.add(getString(R.string.CG_QualityOriginal)); values.add(CherrygramExperimentalConfig.HLS_QUALITY_ORIGINAL);
        labels.add("1440p"); values.add(CherrygramExperimentalConfig.HLS_QUALITY_1440);
        labels.add("1080p"); values.add(CherrygramExperimentalConfig.HLS_QUALITY_1080);
        labels.add("720p"); values.add(CherrygramExperimentalConfig.HLS_QUALITY_720);
        labels.add(getString(R.string.CG_QualityLow)); values.add(CherrygramExperimentalConfig.HLS_QUALITY_LOW);
        PopupHelper.show(labels, getString(R.string.CG_DefaultHlsVideoQuality), values.indexOf(CherrygramExperimentalConfig.INSTANCE.getDefaultHlsVideoQuality()), getContext(), index -> { CherrygramExperimentalConfig.INSTANCE.setDefaultHlsVideoQuality(values.get(index)); if (after != null) after.run(); });
    }

    private String getVideoDecoderValue() {
        return switch (CherrygramExperimentalConfig.INSTANCE.getVideoPlayerDecoder()) {
            case CherrygramExperimentalConfig.VIDEO_DECODER_HARDWARE -> getString(R.string.CG_VideoDecoderHardware);
            case CherrygramExperimentalConfig.VIDEO_DECODER_PREFER_HARDWARE -> getString(R.string.CG_VideoDecoderPreferHardware);
            default -> getString(R.string.CG_VideoDecoderPreferSoftware);
        };
    }

    private void showVideoDecoderSelector(Runnable after) {
        ArrayList<String> labels = new ArrayList<>(); ArrayList<Integer> values = new ArrayList<>();
        labels.add(getString(R.string.CG_VideoDecoderHardware)); values.add(CherrygramExperimentalConfig.VIDEO_DECODER_HARDWARE);
        labels.add(getString(R.string.CG_VideoDecoderPreferHardware)); values.add(CherrygramExperimentalConfig.VIDEO_DECODER_PREFER_HARDWARE);
        labels.add(getString(R.string.CG_VideoDecoderPreferSoftware)); values.add(CherrygramExperimentalConfig.VIDEO_DECODER_PREFER_SOFTWARE);
        PopupHelper.show(labels, getString(R.string.CG_VideoPlayerDecoder), values.indexOf(CherrygramExperimentalConfig.INSTANCE.getVideoPlayerDecoder()), getContext(), index -> { CherrygramExperimentalConfig.INSTANCE.setVideoPlayerDecoder(values.get(index)); if (after != null) after.run(); });
    }


}
