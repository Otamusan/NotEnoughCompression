package otamusan.client.blockcompressed;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import otamusan.blocks.BlockCompressed;

/**
 * Created by TheGreyGhost on 19/04/2015. This class is used to customise the
 * rendering of the camouflage block, based on the block it is copying.
 */
public class BlockCompressedBakedModel implements IBakedModel {
	private final @Nonnull IBakedModel baseModel;

	public BlockCompressedBakedModel(IBakedModel baseModel) {
		this.baseModel = baseModel;
	}

	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		IBlockState state_child = null;

		// BlockStateを取得
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState sBlockState = (IExtendedBlockState) state;
			IBlockState orstate = sBlockState.getValue(BlockCompressed.COMPRESSEDBLOCK_STATE);
			state_child = orstate;
		}

		// stateがnullだったらデフォルトモデル
		if (state_child==null)
			return baseModel.getQuads(state_child, side, rand);

		// 破片パーティクルのテクスチャ更新
		this.originalSprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state_child);

		// チェストなどのモデルを使用しないブロックは描画しない
		if (state_child.getRenderType()!=EnumBlockRenderType.MODEL)
			return Lists.newArrayList();

		// モデルを取得
		IBakedModel model_child = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state_child);
		List<BakedQuad> originallist = model_child.getQuads(state_child, side, rand);

		List<BakedQuad> list = originallist.stream().map(qued -> {
			return new CompressedBakedQuad(
					qued.getVertexData(), 1, qued.getFace(), qued.getSprite(),
					qued.shouldApplyDiffuseLighting(), qued.getFormat(), qued.getTintIndex());
		}).collect(Collectors.toList());

		return list;
	}

	private TextureAtlasSprite originalSprite;

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return originalSprite;
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
	public ItemOverrideList getOverrides() {
		return baseModel.getOverrides();
	}

	@SuppressWarnings("deprecation")
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return baseModel.getItemCameraTransforms();
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(
			ItemCameraTransforms.TransformType cameraTransformType) {
		Matrix4f matrix4f = baseModel.handlePerspective(cameraTransformType).getRight();
		return Pair.of(this, matrix4f);
	}
}