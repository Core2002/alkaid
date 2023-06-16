/*
 * Copyright 2022 Alkaid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alkaidmc.alkaid.metadata.stream;

import com.alkaidmc.alkaid.metadata.MetadataContainer;
import com.alkaidmc.alkaid.metadata.nbt.NBTCompound;
import com.alkaidmc.alkaid.metadata.nbt.NBTData;
import com.google.common.base.Verify;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Milkory
 */
@Accessors(fluent = true)
public record ContainerComponentStream(ContainerComponentStream root, NBTCompound compound, MetadataContainer owner) implements ContainerStream {

    public ContainerComponentStream(ContainerComponentStream root, NBTCompound compound, MetadataContainer owner) {
        this.root = root == null ? this : root;
        this.compound = compound;
        this.owner = owner;
    }

    public ContainerComponentStream access(String key) {
        return new ContainerComponentStream(root, compound.getCompound(key), owner);
    }

    public ContainerComponentStream access(String key, Consumer<ContainerComponentStream> then) {
        then.accept(new ContainerComponentStream(root, compound.getCompound(key), owner));
        return this;
    }

    @SuppressWarnings("all")
    public ContainerComponentStream access(Object... keys) {
        ContainerStream current = this;
        for (int i = 0; i < keys.length; i++) {
            var key = keys[i];
            var nextKey = i + 1 < keys.length ? keys[i + 1] : null;
            if (key instanceof Integer) {
                if (nextKey instanceof Integer) {
                    current = ((ContainerListStream) current).accessList((int) key);
                } else {
                    current = ((ContainerListStream) current).access((int) key);
                }
            } else if (key instanceof String) {
                if (nextKey instanceof Integer) {
                    current = ((ContainerComponentStream) current).accessList((String) key);
                } else {
                    current = ((ContainerComponentStream) current).access((String) key);
                }
            } else {
                throw new IllegalArgumentException("Only string and integer are allowed");
            }
        }
        return (ContainerComponentStream) current;
    }

    @SuppressWarnings("all")
    public ContainerListStream accessList(Object... keys) {
        ContainerStream current = this;
        for (int i = 0; i < keys.length; i++) {
            var key = keys[i];
            var nextKey = i + 1 < keys.length ? keys[i + 1] : null;
            if (key instanceof Integer) {
                if (nextKey instanceof String) {
                    current = ((ContainerListStream) current).access((int) key);
                } else {
                    current = ((ContainerListStream) current).accessList((int) key);
                }
            } else if (key instanceof String) {
                if (nextKey instanceof String) {
                    current = ((ContainerComponentStream) current).access((String) key);
                } else {
                    current = ((ContainerComponentStream) current).accessList((String) key);
                }
            } else {
                throw new IllegalArgumentException("Only string and integer are allowed");
            }
        }
        return (ContainerListStream) current;
    }

    public ContainerListStream accessList(String key) {
        return new ContainerListStream(root, compound.getList(key), owner);
    }

    public ContainerComponentStream accessList(String key, Consumer<ContainerListStream> then) {
        then.accept(new ContainerListStream(root, compound.getList(key), owner));
        return this;
    }

    public ContainerComponentStream merge(NBTCompound compound) {
        compound.merge(compound);
        return this;
    }

    public ContainerComponentStream set(String key, byte value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, boolean value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, short value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, int value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, long value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, float value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, double value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, byte[] value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, String value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, List<NBTData> value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, NBTCompound value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, NBTCompound.Builder builder) {
        compound.set(key, builder.build());
        return this;
    }

    public ContainerComponentStream set(String key, int[] value) {
        compound.set(key, value);
        return this;
    }

    public ContainerComponentStream set(String key, long[] value) {
        compound.set(key, value);
        return this;
    }

    public void save() {
        if (root == this) {
            compound.saveTo(owner);
        } else {
            root.save();
        }
    }

}
