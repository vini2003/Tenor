package tenor.network;

import net.minecraft.util.math.BlockPos;

public class NetworkNode {
    public BlockPos position;

    public static NetworkNode of(BlockPos position) {
        return new NetworkNode().setPosition(position);
    }

    public NetworkNode setPosition(BlockPos position) {
        this.position = position;
        return this;
    }

    public BlockPos getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NetworkNode)) return false;
        return ((NetworkNode) object).position.equals(position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}
