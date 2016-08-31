package me.binarynetwork.core.common.item;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by Bench on 8/28/2016.
 */
public class ItemPredicate implements Predicate<ItemStack> {


    private Set<Material> materials;

    private Set<MaterialData> materialDatas;

    private boolean whiteList;

    public ItemPredicate()
    {
        this(null, null, true);
    }

    public ItemPredicate(boolean whiteList)
    {
        this(null, null, whiteList);
    }

    public ItemPredicate(Collection<Material> materials)
    {
        this(materials, null, true);
    }

    public ItemPredicate(Collection<Material> materials, boolean whiteList)
    {
        this(materials, null, whiteList);
    }

    public ItemPredicate(Collection<Material> materials, Collection<MaterialData> materialDatas)
    {
        this(materials, materialDatas, true);
    }

    public ItemPredicate(Collection<Material> materials, Collection<MaterialData> materialDatas, boolean whiteList)
    {
        this.materials = new HashSet<>();
        this.materialDatas = new HashSet<>();

        if (materials != null)
            this.materials.addAll(materials);

        if (materialDatas != null)
            this.materialDatas.addAll(materialDatas);

        this.whiteList = whiteList;
    }

    @Override
    public boolean test(ItemStack item)
    {
        return this.whiteList == (materials.contains(item.getType()) || materialDatas.contains(item.getData()));
    }

    public boolean addMaterial(Material material)
    {
        return this.materials.add(material);
    }

    public boolean removeMaterial(Material material)
    {
        return this.materials.remove(material);
    }

    public boolean addMaterialData(MaterialData materialData)
    {
        return this.materialDatas.add(materialData);
    }

    public boolean removeMaterialData(MaterialData materialData)
    {
        return this.materialDatas.remove(materialData);
    }

    public boolean isWhiteList()
    {
        return whiteList;
    }

    public void setWhiteList(boolean whiteList)
    {
        this.whiteList = whiteList;
    }
}
