package me.binarynetwork.core.commands.commands;

import com.sun.jna.platform.win32.WinDef;
import me.binarynetwork.core.command.BaseCommand;
import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.permissions.PermissionManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bench on 9/21/2016.
 */
public class BackGroundCommand extends BaseCommand {
    public BackGroundCommand(PermissionManager permissionManager)
    {
        super(permissionManager, "command.background");
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args)
    {
        if (args.length < 1)
            return false;
        File file = new File(args[0]);
        if (!file.exists())
            sender.sendMessage("File: " + file.getPath() + " not found!");
        change(file.getAbsolutePath());
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args)
    {

        File parrent = new File(FilenameUtils.getFullPathNoEndSeparator(args[0]));

        if (!parrent.exists())
            return null;

        String partialName = FilenameUtils.getName(args[0]);

        List<File> files = FileUtil.getSubDirectoriesThatStartWith(parrent, partialName);
        List<String> tab = new ArrayList<>();
        for (File file : files)
        {
            tab.add(file.getPath());
        }
        return tab;
    }

    public static void change(String path)
    {
        WallpaperChanger.SPI.INSTANCE.SystemParametersInfo(
                new WinDef.UINT_PTR(WallpaperChanger.SPI.SPI_SETDESKWALLPAPER),
                new WinDef.UINT_PTR(0),
                path,
                new WinDef.UINT_PTR(WallpaperChanger.SPI.SPIF_UPDATEINIFILE | WallpaperChanger.SPI.SPIF_SENDWININICHANGE));
    }

}
