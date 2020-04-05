package tenor.block.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.particle.ParticleTypes;
import org.lwjgl.opengl.GL11;
import tenor.block.WireNodeBlock;
import tenor.block.entity.WireNodeBlockEntity;
import tenor.initialize.TenorEnergies;
import tenor.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WireNodeBlockEntityRenderer extends BlockEntityRenderer<WireNodeBlockEntity> {
	public WireNodeBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(WireNodeBlockEntity be, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		if (!NetworkManager.INSTANCE.get(TenorEnergies.ENERGY_TYPE, be.getPos()).isNullOrEmpty()) {
			be.getWorld().addParticle(ParticleTypes.SMOKE, be.getPos().getX(), be.getPos().getY(), be.getPos().getZ(), 0, 0, 0);
		}

		for (WireNodeBlockEntity ch : be.children) {


			double oY = WireNodeBlock.OFFSETS.get(((WireNodeBlock) be.getCachedState().getBlock()).tier).get(0);

			double x1 = be.getPos().getX();
			double y1 = be.getPos().getY();
			double z1 = be.getPos().getZ();

			switch (be.getWorld().getBlockState(be.getPos()).get(WireNodeBlock.FACING)) {
				case NORTH:
					x1 += 0.5f;
					y1 += 0.5f;
					z1 += 1f - oY;
					break;
				case SOUTH:
					x1 += 0.5f;
					y1 += 0.5f;
					z1 += oY;
					break;
				case WEST:
					x1 += 1f - oY;
					y1 += 0.5f;
					z1 += 0.5f;
					break;
				case EAST:
					x1 += oY;
					y1 += 0.5f;
					z1 += 0.5f;
					break;
				case UP:
					x1 += 0.5f;
					y1 += oY;
					z1 += 0.5f;
					break;
				case DOWN:
					x1 += 0.5f;
					y1 += 1 - oY;
					z1 += 0.5f;
					break;
			}

			double x3 = ch.getPos().getX();
			double y3 = ch.getPos().getY();
			double z3 = ch.getPos().getZ();

			oY = WireNodeBlock.OFFSETS.get(((WireNodeBlock) ch.getCachedState().getBlock()).tier).get(0);

			switch (ch.getWorld().getBlockState(ch.getPos()).get(WireNodeBlock.FACING)) {
				case NORTH:
					x3 += 0.5f;
					y3 += 0.5f;
					z3 += 1f - oY;
					break;
				case SOUTH:
					x3 += 0.5f;
					y3 += 0.5f;
					z3 += oY;
					break;
				case WEST:
					x3 += 1f - oY;
					y3 += 0.5f;
					z3 += 0.5f;
					break;
				case EAST:
					x3 += oY;
					y3 += 0.5f;
					z3 += 0.5f;
					break;
				case UP:
					x3 += 0.5f;
					y3 += oY;
					z3 += 0.5f;
					break;
				case DOWN:
					x3 += 0.5f;
					y3 += 1 - oY;
					z3 += 0.5f;
					break;
			}

			double x2 = x3 < 0 ? ((x3 - x1) / 2) : ((x3 + x1)) / 2;
			double y2 = ((y1 + y3) / 2) - (0.015 * (y1 + y3));

			double dZ = (z3 - z1) / 100;
			double cZ = 0;

			List<Vector3f> positions = new ArrayList<>();

			for (double t = 0; t < 1; t += 0.01) {
				// C(t)=(1−t)2P0+2t(1−t)P1+t2P2

				double p0M = Math.pow(1 - t, 2);
				double p0X = x1 * p0M;
				double p0Y = y1 * p0M;

				double p1M = 2 * t * (1 - t);
				double p1X = p1M * x2;
				double p1Y = p1M * y2;

				double p2M = Math.pow(t, 2);
				double p2X = p2M * x3;
				double p2Y = p2M * y3;

				double pX = p0X + p1X + p2X;
				double pY = p0Y + p1Y + p2Y;

				positions.add(new Vector3f((float) pX, (float) pY, (float) ((float) z1 + cZ)));

				cZ += dZ;
			}

			Vector3f origin = new Vector3f((float) x1, (float) y1, (float) z1);
			Vector3f previous = origin;

			matrixStack.push();

			VertexConsumer consumer = vertexConsumerProvider.getBuffer(RenderLayer.getLines());

			for (Vector3f vector : positions) {
				if (vector != origin) {
					float xA = vector.getX() - be.getPos().getX();
					float xB = previous.getX() - be.getPos().getX();

					float yA = vector.getY() - be.getPos().getY();
					float yB = previous.getY() - be.getPos().getY();

					float zA = vector.getZ() - be.getPos().getZ();
					float zB = previous.getZ() - be.getPos().getZ();

					consumer.vertex(matrixStack.peek().getModel(), xA, yA, zA).color(0, 255, 0, 255).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(255).normal(matrixStack.peek().getNormal(), 0, 1, 0).next();
					consumer.vertex(matrixStack.peek().getModel(), xB, yB, zB).color(255, 0, 0, 255).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(255).normal(matrixStack.peek().getNormal(), 0, 1, 0).next();

				}
				previous = vector;
			}

			matrixStack.pop();
		}

	}
}
