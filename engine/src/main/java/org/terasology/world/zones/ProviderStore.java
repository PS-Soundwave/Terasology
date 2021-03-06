/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.world.zones;

import com.google.common.collect.ImmutableList;
import org.terasology.world.generation.EntityProvider;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generation.WorldRasterizer;

import java.util.ArrayList;
import java.util.List;

/**
 * A ProviderStore acts as a building block for the world generator, allowing facet providers, entity providers,
 * rasterizers, and zones to be added to them. Both the {@link WorldBuilder} and {@link Zone} are ProviderStores,
 * and zones can be nested into other zones.
 */
public abstract class ProviderStore {

    protected Long seed;

    private final List<Zone> childZones = new ArrayList<>();

    public abstract ProviderStore addProvider(FacetProvider facet);

    public abstract ProviderStore addEntities(EntityProvider entityProvider);

    public abstract ProviderStore addRasterizer(WorldRasterizer rasterizer);

    /**
     * Add a zone to this ProviderStore.
     *
     * This adds the zone as a child, sets the zone's parents, and adds the providers and rasterizers appropriately.
     *
     * @param zone the zone to add
     * @return this
     */
    public ProviderStore addZone(Zone zone) {
        childZones.add(zone);
        zone.setParent(this);

        zone.getFacetProviders().forEach(this::addProvider);
        addRasterizer(zone);
        addEntities(zone);

        return this;
    }

    /**
     * Set the seed to be used in world generation.
     *
     * Set the seed of this and of all of the child zones (which recursively sets the seed for every zone in the tree).
     *
     * @param seed the world generation seed
     */
    public void setSeed(long seed) {
        this.seed = seed;
        for (Zone zone : getChildZones()) {
            zone.setSeed(seed);
        }
    }

    /**
     * @return the world generation seed
     */
    public long getSeed() {
        return seed;
    }

    /**
     * @return the list of zones which are direct children of this
     */
    public ImmutableList<Zone> getChildZones() {
        return ImmutableList.copyOf(childZones);
    }

    public Zone getChildZone(String name) {
        return getChildZones().stream()
                .filter(z -> z.getName().equals(name))
                .reduce((a, b) -> a)
                .orElseThrow(() -> new IllegalStateException("No zone with name " + name));
    }

}
