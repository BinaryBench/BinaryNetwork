package me.binarynetwork.core.common.utils;

import me.binarynetwork.core.common.Log;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.Zip4jUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Bench on 9/2/2016.
 */
public class FileUtil {

    /**
     * Size of the buffer to read/write data
     *
     */
    private static final int BUFFER_SIZE = 4096;


    public static File newFileIgnoreCase(@Nonnull File parent, @Nonnull String child, String... childnames)
    {
        List<String> names = new ArrayList<>();
        names.add(child);
        names.addAll(Arrays.asList(childnames));

        if (!parent.isDirectory())
            return new File(parent, child);
        File[] files = parent.listFiles();
        if (files == null)
            files = new File[]{};

        for (File file : files)
            for (String name : names)
                if (file.getName().equalsIgnoreCase(name))
                    return file;

        return new File(parent, child);
    }

    public static List<String> loadTextFile(File file)
    {
        if (file.exists())
        {
            try
            {
                FileReader fileReader = new FileReader(file);

                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line;

                List<String> mapinfo = new ArrayList<>();

                while ((line = bufferedReader.readLine()) != null)
                {
                    mapinfo.add(line);
                }

                bufferedReader.close();

                return mapinfo;

            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static boolean copy(@Nonnull File sourceFile, @Nonnull File destFile)
    {
        return copy(sourceFile, destFile, x -> true);
    }

    public static boolean copy(@Nonnull File sourceFile, @Nonnull File destFile, Predicate<File> filePredicate)
    {
        if (sourceFile.isDirectory())
            return copyDirectory(sourceFile, destFile, filePredicate);
        else
            if (isZipped(sourceFile))
                return unZip(sourceFile, destFile, filePredicate);
            else
                if (filePredicate.test(sourceFile))
                    return copyFile(sourceFile, destFile);
        return false;
    }

    public static boolean copyFile(@Nonnull File sourceFile, @Nonnull File destFile)
    {
        try
        {
            FileUtils.copyFile(sourceFile, destFile);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyDirectory(@Nonnull File sourceFile, @Nonnull File destFile, Predicate<File> filePredicate)
    {
        try
        {
            FileUtils.copyDirectory(sourceFile, destFile, filePredicate::test);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean unZip(@Nonnull String zipFilePath, @Nonnull String destDirPath)
    {
        return unZip(new File(zipFilePath), new File(destDirPath));
    }

    public static boolean unZip(@Nonnull File zipFile, @Nonnull File destDirectory)
    {
        return unZip(zipFile, destDirectory, x -> true);
    }

    public static boolean unZip(@Nonnull String zipFilePath, @Nonnull String destDirPath, @Nonnull Predicate<File> filePredicate)
    {
        return unZip(new File(zipFilePath), new File(destDirPath), filePredicate);
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     *
     * @param zipFile the zip file
     * @param destDirectory the destination file
     */
    public static boolean unZip(@Nonnull File zipFile, @Nonnull File destDirectory, @Nonnull Predicate<File> filePredicate)
    {
        try
        {
            if (!destDirectory.exists())
            {
                destDirectory.mkdir();
            }

            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry entry;

            Set<File> ignoredFiles = new LinkedHashSet<>();

            // iterates over entries in the zip file
            OUTER_WHILE:
            while ((entry = zipIn.getNextEntry()) != null)
            {
                File entryFile = new File(destDirectory, entry.getName());

                for (File file : ignoredFiles)
                    if (isSubDirectory(file, entryFile))
                        continue OUTER_WHILE;

                if (!filePredicate.test(entryFile))
                {
                    ignoredFiles.add(entryFile);
                    continue;
                }

                if (!entry.isDirectory())
                {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, entryFile);
                }
                else
                {
                    // if the entry is a directory, make the directory
                    entryFile.mkdir();
                }
                zipIn.closeEntry();
            }
            zipIn.close();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *      * Extracts a zip entry (file entry)
     *      * @param zipIn
     *      * @param destFile
     *      * @throws IOException
     *      
     */
    private static void extractFile(ZipInputStream zipIn, File destFile) throws IOException
    {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1)
        {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public static boolean isSubDirectory(File parent, File child)
    {
        try
        {
            return isSubDirectory(parent.getCanonicalPath(), child.getCanonicalPath());
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isSubDirectory(String parent, String child)
    {
        return child.startsWith(parent);
    }

    public static boolean isZipped(@Nonnull File file)
    {
        if (file.isDirectory())
            return false;
        try
        {
            return new ZipInputStream(new FileInputStream(file)).getNextEntry() != null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
