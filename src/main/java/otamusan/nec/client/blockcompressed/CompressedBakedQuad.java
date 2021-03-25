package otamusan.nec.client.blockcompressed;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

public class CompressedBakedQuad extends BakedQuad {
    private int oriTintIndex;

    public CompressedBakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn,
                               boolean applyDiffuseLighting, VertexFormat format, int oriTintIndex) {
        super(vertexDataIn, tintIndexIn, faceIn, spriteIn, applyDiffuseLighting, format);
        this.oriTintIndex = oriTintIndex;
    }

    public int getOriTintIndex() {
        return oriTintIndex;
    }

    @Override
    public int getTintIndex() {
        return oriTintIndex + 100;
    }
}
