/*
 * MIT License
 *
 * Copyright (c) 2020-2023 William Blake Galbreath
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.pl3x.map.core.configuration;

import java.nio.file.Path;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.util.FileUtil;

@SuppressWarnings("CanBeFinal")
public final class Config extends AbstractConfig {
    @Key("settings.debug-mode")
    @Comment("""
            Extra logger/console output. (can be spammy)""")
    public static boolean DEBUG_MODE = false;

    @Key("settings.language-file")
    @Comment("""
            The language file to use from the locale folder.""")
    public static String LANGUAGE_FILE = "lang-en.yml";

    @Key("settings.web-directory.path")
    @Comment("""
            The directory that houses the website and world tiles.
            This is a relative path from Pl3xMap's plugin directory,
            unless it starts with / in which case it will be treated
            as an absolute path.""")
    public static String WEB_DIR = "web/";
    @Key("settings.web-directory.read-only")
    @Comment("""
            Set to true if you don't want Pl3xMap to overwrite
            the website files on startup. (Good for servers that
            customize these files)
            Note: Be sure this is false when upgrading.""")
    public static boolean WEB_DIR_READONLY = false;
    @Key("settings.web-directory.tile-format")
    @Comment("""
            The image format for tile images.
            Built in types: bmp, gif, jpeg, png""")
    public static String WEB_TILE_FORMAT = "png";
    @Key("settings.web-directory.tile-quality")
    @Comment("""
            The quality for image tiles (0.0 - 1.0)
            0.0 is low quality, high compression, small file size
            1.0 is high quality, no compression, large file size
            Note: Not all image formats honor this setting.""")
    public static double WEB_TILE_QUALITY = 0.0F;

    @Key("settings.internal-webserver.enabled")
    @Comment("""
            Enable the built-in web server.
            Disable this if you want to use a standalone web server
            such as apache or nginx.""")
    public static boolean HTTPD_ENABLED = true;
    @Key("settings.internal-webserver.bind")
    @Comment("""
            The interface the built-in web server should bind to.
            This is NOT always the same as your public facing IP.
            If you don't understand what this is,
            leave it set to 0.0.0.0""")
    public static String HTTPD_BIND = "0.0.0.0";
    @Key("settings.internal-webserver.port")
    @Comment("""
            The port the built-in web server listens to.
            Make sure the port is allocated if using Pterodactyl.""")
    public static int HTTPD_PORT = 8080;

    @Key("settings.performance.render-threads")
    @Comment("""
            The number of process-threads to use for loading and scanning chunks.
            Value of -1 will use 50% of the available cpu-threads. (recommended)""")
    public static int RENDER_THREADS = -1;

    @Key("settings.performance.gc.when-finished")
    @Comment("""
            Runs the JVM GC after a render job stops to free up memory immediately.""")
    public static boolean GC_WHEN_FINISHED = true;

    @Key("settings.performance.gc.when-running")
    @Comment("""
            Runs the JVM GC aggressively while a render is running
            CAUTION: this _will_ slow down your renders!""")
    public static boolean GC_WHEN_RUNNING = false;

    private static final Config CONFIG = new Config();

    public static void reload() {
        Path mainDir = Pl3xMap.api().getMainDir();

        // extract default config from jar
        FileUtil.extractFile(Config.class, "config.yml", mainDir, false);

        CONFIG.reload(mainDir.resolve("config.yml"), Config.class);
    }
}
