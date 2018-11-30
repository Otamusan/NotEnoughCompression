package otamusan.client.BlockCompressed;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import otamusan.blocks.BlockCompressed;

/**
 * Created by TheGreyGhost on 19/04/2015. This class is used to customise the
 * rendering of the camouflage block, based on the block it is copying.
 */
public class BlockCompressedBakedModel implements IBakedModel {

	public BlockCompressedBakedModel(IBakedModel model) {
		originalModel = model;
	}

	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {

		List<BakedQuad> originallist = handleBlockState(state).getQuads(getState(state), side, rand);

		ArrayList<BakedQuad> list = new ArrayList<>();

		for (int i = 0; i < originallist.size(); i++) {
			BakedQuad qued = originallist.get(i);
			BakedQuad newqued = new BakedQuad(qued.getVertexData(), 1, qued.getFace(), qued.getSprite(),
					qued.shouldApplyDiffuseLighting(), qued.getFormat());
			list.add(newqued);
		}

		return list;
	}

	private IBlockState getState(IBlockState state) {
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState sBlockState = (IExtendedBlockState) state;
			IBlockState orstate = sBlockState.getValue(BlockCompressed.COMPRESSEDBLOCK_STATE);
			return orstate;
		}
		return Blocks.STONE.getDefaultState();
	}

	private IBakedModel handleBlockState(@Nullable IBlockState iBlockState) {
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher()
				.getModelForState(getState(iBlockState));
		this.originalModel = model;
		return model;
	}

	private IBakedModel originalModel;

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return originalModel.getParticleTexture();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return originalModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return originalModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return originalModel.isBuiltInRenderer();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return originalModel.getOverrides();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return originalModel.getItemCameraTransforms();
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(
			ItemCameraTransforms.TransformType cameraTransformType) {
		Matrix4f matrix4f = originalModel.handlePerspective(cameraTransformType).getRight();
		return Pair.of(this, matrix4f);
	}

}