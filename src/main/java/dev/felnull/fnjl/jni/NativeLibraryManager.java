package dev.felnull.fnjl.jni;

import dev.felnull.fnjl.FelNullJavaLibrary;
import dev.felnull.fnjl.os.OSs;
import dev.felnull.fnjl.util.FNDataUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * JNIを利用したネイティブライブラリ管理
 *
 * @author MORIMORI0317
 * @since 1.10
 */
public class NativeLibraryManager {
    private static boolean inited;
    private static boolean load;
    private static Path libraryFolder;

    /**
     * JNIライブラリを読み込む
     */
    public static void loadLibrary() {
        if (inited)
            return;
        inited = true;
        OSs.Type os = OSs.getOS();
        try {
            if (os == OSs.Type.WINDOWS && OSs.isX64()) {
                load = loadExtractLibrary(OSs.Type.WINDOWS, "x64");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 読み込みに失敗したかどうか
     *
     * @return 失敗
     */
    public static boolean isLoadFailure() {
        return inited && !load;
    }

    /**
     * ライブラリを抽出するパスを指定
     *
     * @param path 指定パス
     */
    public static void setLibraryFolderPath(Path path) {
        libraryFolder = path;
    }

    private static Path getNativeLibraryFolder() {
        if (libraryFolder != null)
            return libraryFolder;
        return Paths.get(".");
    }

    private static boolean loadExtractLibrary(OSs.Type os, String aarch) throws IOException {
        String name = "FNJL" + FelNullJavaLibrary.getNativeLibVersion() + aarch + "." + os.getLibName();
        File fil = getNativeLibraryFolder().resolve(name).toFile();
        if (fil.exists()) {
            try {
                System.load(fil.getAbsolutePath());
                return true;
            } catch (Throwable ex) {
                ex.printStackTrace();
                Files.delete(fil.toPath());
                extractLibrary(os, aarch);
                System.load(fil.getAbsolutePath());
                return true;
            }
        } else {
            try {
                extractLibrary(os, aarch);
                System.load(fil.getAbsolutePath());
                return true;
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    private static void extractLibrary(OSs.Type os, String aarch) throws IOException {
        String libname = "FNJL" + aarch + "." + os.getLibName();
  /*      String cn = FelNullJavaLibrary.class.getSimpleName();
        String pn = FelNullJavaLibrary.class.getName();
        String ppa = pn.substring(0, pn.length() - cn.length() - 1).replace(".", "/");
        String pp = ppa + "/natives/" + libname;
        */
        String pp = "dev/felnull/fnjl/natives/" + libname;
        //dev/felnull/imp/libs/dev/felnull/fnjl/dev/felnull/imp/libs/natives/FNJLx64.dll:
        InputStream stream = loadResource(pp);

        if (stream == null)
            throw new IOException("Library does not exist: " + pp);

        String name = "FNJL" + FelNullJavaLibrary.getNativeLibVersion() + aarch + "." + os.getLibName();
        Path path = getNativeLibraryFolder().resolve(name);

        Files.write(path, FNDataUtil.streamToByteArray(stream));
    }

    private static InputStream loadResource(String path) {
        InputStream stream = NativeLibraryManager.class.getResourceAsStream("/" + path);
        if (stream == null)
            stream = ClassLoader.getSystemResourceAsStream(path);
        return stream;
    }

}
