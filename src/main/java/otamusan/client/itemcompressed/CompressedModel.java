package otamusan.client.itemcompressed;

import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class CompressedModel implements IBakedModel {
	public CompressedModel(IBakedModel model) {
		this.model = model;
		this.overrideList = new CompressedItemOverrideList(Collections.EMPTY_LIST);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return model.getParticleTexture();
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return model.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return model.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return overrideList;
	}

	private IBakedModel model;
	private CompressedItemOverrideList overrideList;

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(
			ItemCameraTransforms.TransformType cameraTransformType) {
		Matrix4f matrix4f = model.handlePerspective(cameraTransformType).getRight();
		return Pair.of(this, matrix4f);
	}
}