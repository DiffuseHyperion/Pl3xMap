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
package net.pl3x.map.core.renderer;

import net.pl3x.map.core.registry.RendererRegistry;
import net.pl3x.map.core.renderer.task.RegionScanTask;
import net.pl3x.map.core.world.Chunk;
import net.pl3x.map.core.world.Region;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class NightRenderer extends Renderer {
    private Renderer basic;

    public NightRenderer(@NonNull RegionScanTask task, @NonNull Builder builder) {
        super(task, builder);
    }

    public void scanData(@NonNull Region region) {
        // get the basic renderer so we can copy its tiles
        this.basic = getRegionScanTask().getRenderer(RendererRegistry.BASIC);
        super.scanData(region);
    }

    @Override
    public void scanBlock(@NonNull Region region, @NonNull Chunk chunk, Chunk.@NonNull BlockData data, int blockX, int blockZ) {
        // get basic pixel color
        int pixelColor;
        if (this.basic != null) {
            // get current color from basic renderer
            pixelColor = this.basic.getTileImage().getPixel(blockX, blockZ);
        } else {
            // could not find basic renderer (disabled?), we have to draw it ourselves
            pixelColor = basicPixelColor(region, data, blockX, blockZ);
        }

        // get light level right above this block
        int lightPixel = calculateLight(chunk, data.getFluidState(), blockX, data.getBlockY(), blockZ, data.getFluidY(), pixelColor);
        getTileImage().setPixel(blockX, blockZ, lightPixel);
    }
}
