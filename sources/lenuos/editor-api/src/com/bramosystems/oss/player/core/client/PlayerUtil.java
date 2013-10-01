/*
 * Copyright 2009 Sikirulai Braheem
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.core.client;

import java.util.Set;

import com.bramosystems.oss.player.core.client.impl.plugin.PluginInfo;
import com.bramosystems.oss.player.core.client.impl.plugin.PluginManager;
import com.bramosystems.oss.player.core.client.ui.DivXPlayer;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.bramosystems.oss.player.util.client.RegExp;
import com.bramosystems.oss.player.util.client.RegExp.RegexException;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Utility class for various media player related functions.
 *
 * @author Sikirulai Braheem
 */
public class PlayerUtil {

    private static NumberFormat timeFormat = NumberFormat.getFormat("00");
    private static NumberFormat hourFormat = NumberFormat.getFormat("#0");

    /**
     * Formats the specified time (in milliseconds) into time string in the
     * format <code>hh:mm:ss</code>.
     *
     * <p>The hours part of the formatted time is omitted if {@code milliseconds} time
     * is less than 60 minutes.
     *
     * @param milliSeconds media time to be formatted
     * @return the formatted time as String
     */
    public static String formatMediaTime(long milliSeconds) {
        long secth = 0, secs = 0, min = 0, hrs = 0;

        try {
            secth = milliSeconds % 1000;    // millisecs.
            milliSeconds /= 1000;

            secs = milliSeconds % 60;   // secs.
            milliSeconds /= 60;

            min = milliSeconds % 60;   // min.
            milliSeconds /= 60;

            hrs = milliSeconds % 60;   // hrs.
            milliSeconds /= 60;
        } catch (Exception e) {
            // catch exceptions like division by zero...
        }

        String time = (hrs > 0 ? hourFormat.format(hrs) + ":" : "")
                + timeFormat.format(min) + ":" + timeFormat.format(secs);
        return time;
    }
    
    

    /**
     * Utility method to get a player that best supports the specified {@code mediaURL}.
     *
     * <p>A suitable player is determined based on the plugin available on the
     * browser as well as its suitability to playback the specified media.
     *
     * <p><b>NOTE:</b> If the media is served with a special streaming protocol such as
     * MMS and RTSP, {@code mediaURL} should be specified in its absolute form. Otherwise
     * {@code mediaURL} should end in a standard media format extension e.g.
     * {@code .mp3, .wma, .mov, .flv ...}
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @return a suitable player implementation
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required plugin version is not installed on the client.
     * @throws PluginNotFoundException if the required plugin is not installed on the client.
     */
    public static AbstractMediaPlayer getPlayer(String mediaURL,
            boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        return getPlayer(Plugin.Auto, mediaURL, autoplay, height, width);
    }

    /**
     * Utility method to get a player that best supports the specified {@code mediaURL} with
     * the specified plugin feature.
     *
     * <p>A suitable player is determined based on the features/capabilities derivable from
     * the player plugin, its availability on the browser, and its suitability to
     * playback the specified media.
     *
     *<p>The current implementation considers the following features:
     * <ul>
     * <li>{@linkplain Plugin#Auto} : Basic features i.e. ability to play, pause, stop e.t.c</li>
     * <li>{@linkplain Plugin#PlaylistSupport} : Playlist management features i.e.
     * ability to add and/or remove media items from playlists</li>
     * <li>{@linkplain Plugin#MatrixSupport} : Matrix transformation features i.e.
     * ability to perform such operations as rotation, traslation, skewing etc</li>
     * </ul>
     *
     * <p><b>NOTE:</b> If the media is served with a special streaming protocol such as
     * MMS and RTSP, {@code mediaURL} should be specified in its absolute form. Otherwise
     * {@code mediaURL} should end in a standard media format extension e.g.
     * {@code .mp3, .wma, .mov, .flv ...}
     *
     * @param plugin the features of the required player plugin
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @return a suitable player implementation
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required plugin version is not installed on the client.
     * @throws PluginNotFoundException if the required plugin is not installed on the client.
     *
     * @since 1.0
     * @see #getPlayer(String, boolean, String, String)
     */
    public static AbstractMediaPlayer getPlayer(Plugin plugin, String mediaURL,
            boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        return _getPlayer(_getPlugin(plugin, mediaURL), mediaURL, autoplay, height, width);
    }

    /**
     * Utility method to get a player that best supports the specified {@code mediaURL} with
     * the specified plugin feature.
     *
     * @param plugin the features of the required player plugin
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @return a suitable player implementation
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required plugin version is not installed on the client.
     * @throws PluginNotFoundException if the required plugin is not installed on the client.
     *
     * @since 1.1
     * @see #getPlayer(Plugin, String, boolean, String, String)
     */
    public static AbstractMediaPlayer getPlayer(Plugin plugin, String mediaURL, boolean autoplay)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        return _getPlayer(_getPlugin(plugin, mediaURL), mediaURL, autoplay);
    }

    /**
     * Util method to get the appropriate player plugin for the specified plugin feature
     * and media
     *
     * @param plugin
     * @param mediaURL
     * @return appropriate plugin
     * @since 1.1
     */
    private static Plugin _getPlugin(Plugin plugin, String mediaURL) {
        String protocol = extractProtocol(mediaURL);

        String extracted = mediaURL;
        try {
    		RegExp.RegexResult result = RegExp.getRegExp("[^\\?]*", "").exec(mediaURL);
    		extracted = result.getMatch(0);
    	} catch (RegexException e) {}
        
        String ext = extracted.substring(mediaURL.lastIndexOf(".") + 1);
        Plugin pg = Plugin.Auto, _plugins[] = null;
        
        switch (plugin) {
            case MatrixSupport:
                _plugins = new Plugin[2];
                _plugins[0] = Plugin.QuickTimePlayer;
                _plugins[1] = Plugin.FlashPlayer;
                break;
            case PlaylistSupport:
            case Auto:
                _plugins = Plugin.values();
                break;
        }

        if (_plugins != null) {
            for (int i = 0; i < _plugins.length; i++) {
                try {
                    if (canHandleMedia(_plugins[i], protocol, ext)) {
                        pg = _plugins[i];
                        break;
                    }
                } catch (PluginNotFoundException ex) {
                }
            }
            return pg;
        } else {
            return plugin;
        }
    }

    protected static String extractExt(String mediaURL) {
        return mediaURL.substring(mediaURL.lastIndexOf(".") + 1);
    }

    protected static String extractProtocol(String mediaURL) {
        if (mediaURL.contains("://")) {
            return mediaURL.substring(0, mediaURL.indexOf("://"));
        } else {
            return null;
        }
    }
/*
    public static boolean canHandleMedia(Plugin plugin, String mediaURL) {
        String protocol = extractProtocol(mediaURL);
        String ext = extractExt(mediaURL);
        try {
            return canHandleMedia(plugin, protocol, ext);
        } catch (PluginNotFoundException ex) {
            return false;
        }
    }
*/
    protected static boolean canHandleMedia(Plugin plugin, String protocol, String ext) throws PluginNotFoundException {
        PluginVersion pv = PluginManager.getPluginInfo(plugin).getVersion();
        Set<String> types = MimePool.instance.getRegisteredExtensions(plugin);
        Set<String> prots = MimePool.instance.getRegisteredProtocols(plugin);

        if (pv.compareTo(plugin.getVersion()) >= 0) {   // req plugin found...
            // check for streaming protocol & extension ...
            return ((protocol != null) && (prots != null) && prots.contains(protocol.toLowerCase()))
                    || ((ext != null) && (types != null) && types.contains(ext.toLowerCase()));
        }
        return false;
    }

    /**
     * Util method to instantiate the player implementation for the specified plugin
     *
     * @param plugin
     * @param mediaURL
     * @param autoplay
     * @param height
     * @param width
     * @return
     * @throws LoadException
     * @throws PluginVersionException
     * @throws PluginNotFoundException
     */
    private static AbstractMediaPlayer _getPlayer(Plugin plugin, String mediaURL,
            boolean autoplay, String height, String width) throws LoadException,
            PluginVersionException, PluginNotFoundException {
        AbstractMediaPlayer player;
        switch (plugin) {
            case VLCPlayer:
                player = new VLCPlayer(mediaURL, autoplay, height, width);
                break;
            case FlashPlayer:
                player = new FlashMediaPlayer(mediaURL, autoplay, height, width);
                break;
            case QuickTimePlayer:
                player = new QuickTimePlayer(mediaURL, autoplay, height, width);
                break;
            case WinMediaPlayer:
                player = new WinMediaPlayer(mediaURL, autoplay, height, width);
                break;
            case Native:
                player = new NativePlayer(mediaURL, autoplay, height, width);
                break;
            case DivXPlayer:
                player = new DivXPlayer(mediaURL, autoplay, height, width);
                break;
            default:
                throw new PluginNotFoundException();
        }
        return player;
    }

    private static AbstractMediaPlayer _getPlayer(Plugin plugin, String mediaURL,
            boolean autoplay) throws LoadException,
            PluginVersionException, PluginNotFoundException {
        AbstractMediaPlayer player;
        switch (plugin) {
            case VLCPlayer:
                player = new VLCPlayer(mediaURL, autoplay);
                break;
            case FlashPlayer:
                player = new FlashMediaPlayer(mediaURL, autoplay);
                break;
            case QuickTimePlayer:
                player = new QuickTimePlayer(mediaURL, autoplay);
                break;
            case WinMediaPlayer:
                player = new WinMediaPlayer(mediaURL, autoplay);
                break;
            case Native:
                player = new NativePlayer(mediaURL, autoplay);
                break;
            case DivXPlayer:
                player = new DivXPlayer(mediaURL, autoplay);
                break;
            default:
                throw new PluginNotFoundException();
        }
        return player;
    }

    /**
     * Detects the version of the Flash Player plugin available on the clients
     * browser.
     *
     * @return <code>PluginVersion</code> object wrapping the version numbers of the
     * Flash Player on the browser.
     *
     * @throws PluginNotFoundException if a Flash Player plugin could not be found
     * (especially if none is installed or the plugin is disabled).
     */
    public static PluginVersion getFlashPlayerVersion() throws PluginNotFoundException {
        PluginVersion v = PluginManager.getPluginInfo(Plugin.FlashPlayer).getVersion();
        if (v.equals(new PluginVersion())) {
            throw new PluginNotFoundException(Plugin.FlashPlayer);
        }

        return v;
    }

    /**
     * Detects the version of the QuickTime plugin available on the clients browser.
     *
     * @return <code>PluginVersion</code> object wrapping the version numbers of the
     * QuickTime plugin on the browser.
     *
     * @throws PluginNotFoundException if a QuickTime plugin could not be found.
     * (especially if none is installed or the plugin is disabled).
     */
    public static PluginVersion getQuickTimePluginVersion() throws PluginNotFoundException {
        PluginVersion v = PluginManager.getPluginInfo(Plugin.QuickTimePlayer).getVersion();
        if (v.equals(new PluginVersion())) {
            throw new PluginNotFoundException(Plugin.QuickTimePlayer);
        }

        return v;
    }

    /**
     * Detects the version of the Windows Media Player plugin available on the clients browser.
     *
     * @return <code>PluginVersion</code> object wrapping the version numbers of the
     * plugin on the browser.
     *
     * @throws PluginNotFoundException if a plugin could not be found.
     * (especially if none is installed or the plugin is disabled).
     */
    public static PluginVersion getWindowsMediaPlayerPluginVersion() throws PluginNotFoundException {
        PluginVersion v = PluginManager.getPluginInfo(Plugin.WinMediaPlayer).getVersion();
        if (v.equals(new PluginVersion())) {
            throw new PluginNotFoundException(Plugin.WinMediaPlayer);
        }

        return v;
    }

    /**
     * Detects the version of the VLC Media Player plugin available on the clients browser.
     *
     * @return <code>PluginVersion</code> object wrapping the version numbers of the
     * plugin on the browser.
     *
     * @throws PluginNotFoundException if a plugin could not be found.
     * (especially if none is installed or the plugin is disabled).
     *
     * @since 1.0
     */
    public static PluginVersion getVLCPlayerPluginVersion() throws PluginNotFoundException {
        PluginVersion v = PluginManager.getPluginInfo(Plugin.VLCPlayer).getVersion();
        if (v.equals(new PluginVersion())) {
            throw new PluginNotFoundException(Plugin.VLCPlayer);
        }

        return v;
    }

    /**
     * Detects the version of the DivX Web Player plugin available on the clients browser.
     *
     * @return <code>PluginVersion</code> object wrapping the version numbers of the
     * plugin on the browser.
     *
     * @throws PluginNotFoundException if a plugin could not be found.
     * (especially if none is installed or the plugin is disabled).
     *
     * @since 1.2
     */
    public static PluginVersion getDivXPlayerPluginVersion() throws PluginNotFoundException {
        PluginVersion v = PluginManager.getPluginInfo(Plugin.DivXPlayer).getVersion();
        if (v.equals(new PluginVersion())) {
            throw new PluginNotFoundException(Plugin.DivXPlayer);
        }

        return v;
    }

    /**
     * Returns a widget that may be used to notify the user when a required plugin
     * is not available.  The widget provides a link to the plugin download page.
     *
     * <h4>CSS Style Rules</h4>
     * <ul>
     * <li>.player-MissingPlugin { the missing plugin widget }</li>
     * <li>.player-MissingPlugin-title { the title section }</li>
     * <li>.player-MissingPlugin-message { the message section }</li>
     * </ul>
     *
     * @param plugin the missing plugin
     * @param title the title of the message
     * @param message descriptive message to notify user about the missing plugin
     * @param asHTML {@code true} if {@code message} should be interpreted as HTML,
     *          {@code false} otherwise.
     *
     * @return missing plugin widget.
     * @since 0.6
     */
    public static Widget getMissingPluginNotice(final Plugin plugin, String title, String message,
            boolean asHTML) {
        DockPanel dp = new DockPanel()   {

            @Override
            public void onBrowserEvent(Event event) {
                super.onBrowserEvent(event);
                switch (event.getTypeInt()) {
                    case Event.ONCLICK:
                        if (plugin.getDownloadURL().length() > 0) {
                            Window.open(plugin.getDownloadURL(), "dwnload", "");
                        }
                }
            }
        };
        dp.setHorizontalAlignment(DockPanel.ALIGN_LEFT);
        dp.sinkEvents(Event.ONCLICK);
        dp.setWidth("200px");

        Label titleLb = null, msgLb = null;
        if (asHTML) {
            titleLb = new HTML(title);
            msgLb = new HTML(message);
        } else {
            titleLb = new Label(title);
            msgLb = new Label(message);
        }

        dp.add(titleLb, DockPanel.NORTH);
        dp.add(msgLb, DockPanel.CENTER);

        titleLb.setStylePrimaryName("player-MissingPlugin-title");
        msgLb.setStylePrimaryName("player-MissingPlugin-message");
        dp.setStylePrimaryName("player-MissingPlugin");

        DOM.setStyleAttribute(dp.getElement(), "cursor", "pointer");
        return dp;
    }

    /**
     * Convenience method to get a widget that may be used to notify the user when
     * a required plugin is not available.
     *
     * <p>This is same as calling {@code getMissingPluginNotice(plugin, "Missing Plugin",
     *      "<<message>>", false) }
     * <br/>
     * {@literal <<message>>} => [Plugin Name] [version] or later is required to play this media.
     * Click here to get [Plugin Name]
     *
     * @param plugin the required plugin
     * @param version the minimum version of the required plugin
     *
     * @return missing plugin widget.
     * @see #getMissingPluginNotice(Plugin, String, String, boolean)
     * @since 0.6
     */
    public static Widget getMissingPluginNotice(Plugin plugin, String version) {
        String title = "Missing Plugin", message = "";
        switch (plugin) {
            case WinMediaPlayer:
                message = "Windows Media Player " + version + " or later is required to play "
                        + "this media. Click here to get Windows Media Player.";
                break;
            case FlashPlayer:
                message = "Adobe Flash Player " + version + " or later is required to play "
                        + "this media. Click here to get Flash";
                break;
            case QuickTimePlayer:
                message = "QuickTime Player " + version + " plugin or later is required to "
                        + "play this media. Click here to get QuickTime";
                break;
            case VLCPlayer:
                message = "VLC Media Player " + version + " plugin or later is required to "
                        + "play this media. Click here to get VLC Media Player";
                break;
            case DivXPlayer:
                message = "DivX Web Player " + version + " plugin or later is required to "
                        + "play this media. Click here to get DivX Web Player";
                break;
            case Native:
                title = "Browser Not Compliant";
                message = "An HTML 5 compliant browser is required";
                break;
            case PlaylistSupport:
                message = "No player plugin with client-side playlist "
                        + "management can be found";
                break;
            case MatrixSupport:
                message = "No player plugin with matrix transformation "
                        + "capability can be found";
                break;
        }

        return getMissingPluginNotice(plugin, title, message, false);
    }

    /**
     * Convenience method to get a widget that may be used to notify the user when
     * a required plugin is not available.
     *
     * <p>This is same as calling {@code getMissingPluginNotice(plugin, "Missing Plugin",
     *      "<<message>>", false) }
     * <br/>
     * {@literal <<message>>} => [Plugin Name] is required to play this media.
     * Click here to get [Plugin Name]
     *
     * @param plugin the required plugin
     *
     * @return missing plugin widget.
     * @see #getMissingPluginNotice(Plugin, String, String, boolean)
     * @since 0.6
     */
    public static Widget getMissingPluginNotice(Plugin plugin) {
        String title = "Missing Plugin", message = "";
        switch (plugin) {
            case WinMediaPlayer:
                message = "Windows Media Player is required to play "
                        + "this media. Click here to get Windows Media Player";
                break;
            case FlashPlayer:
                message = "Adobe Flash Player is required to play "
                        + "this media. Click here to get Flash";
                break;
            case QuickTimePlayer:
                message = "QuickTime Player is required to play "
                        + "this media. Click here to get QuickTime";
                break;
            case VLCPlayer:
                message = "VLC Media Player is required to play "
                        + "this media. Click here to get VLC Media Player";
                break;
            case DivXPlayer:
                message = "DivX Web Player is required to play "
                        + "this media. Click here to get DivX Web Player";
                break;
            case PlaylistSupport:
                message = "No player plugin with client-side playlist "
                        + "management can be found";
                break;
            case MatrixSupport:
                message = "No player plugin with matrix transformation "
                        + "capability can be found";
                break;
            case Native:
                title = "Browser Not Compliant";
                message = "An HTML 5 compliant browser is required";
                break;
            default:
                message = "A compatible plugin could not be found";
        }
        return getMissingPluginNotice(plugin, title, message, false);
    }

    /**
     * Checks if the browser implements the HTML 5 specification.
     *
     * @return <code>true</code> if browser is HTML 5 compliant, <code>false</code>
     * otherwise
     * @since 1.1
     */
    public static boolean isHTML5CompliantClient() {
        return PluginManager.isHTML5CompliantClient();
    
    }
    
    public static PluginInfo getPlayerPluginInfo(Plugin plugin) throws PluginNotFoundException {
        return PluginManager.getPluginInfo(plugin);
    }
}