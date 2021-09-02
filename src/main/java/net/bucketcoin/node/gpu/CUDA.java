package net.bucketcoin.node.gpu;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.jcurand.JCurand;
import jcuda.jcurand.curandGenerator;
import jcuda.runtime.JCuda;
import net.bucketcoin.node.Miner;
import org.apache.commons.codec.digest.DigestUtils;
import com.sun.jna.*;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;

import static jcuda.jcurand.JCurand.*;
import static jcuda.jcurand.curandRngType.*;
import static jcuda.runtime.JCuda.cudaMalloc;
import static jcuda.runtime.JCuda.cudaMemcpy;
import static jcuda.runtime.cudaMemcpyKind.cudaMemcpyDeviceToHost;
import static jcuda.runtime.JCuda.cudaFree;

public class CUDA {

    public static final int RNG_Algorithm = CURAND_RNG_PSEUDO_PHILOX4_32_10;

    /**
     * Generates 150 hashes using the provided solution number and the block nonce.
     * @param solution Solution number. See {@link Miner}.
     * @param nonce The nonce of the block.
     * @return 150 hashes generated using the parameters given.
     */
    public static String[] md5HexGen(int solution, int nonce) { // Future plans to support GPU mining

        curandGenerator generator = new curandGenerator();

        JCurand.setExceptionsEnabled(true);
        JCuda.setExceptionsEnabled(true);

        // bulk size
        var size = 125;

        int[] hostData = new int[size];
        Pointer deviceData = new Pointer();
        int x = cudaMalloc(deviceData, size * Sizeof.INT);

        curandCreateGenerator(generator, RNG_Algorithm);
        curandSetPseudoRandomGeneratorSeed(generator, solution + nonce);
        curandGenerateUniform(generator, deviceData, size);
        cudaMemcpy(Pointer.to(hostData), deviceData, size * Sizeof.INT, cudaMemcpyDeviceToHost);

        var a = new ArrayList<String>(); // List of hashes made by hashing each CUDA-generated number
        for(int i : hostData) a.add(DigestUtils.md5Hex(String.valueOf(i)));

        curandDestroyGenerator(generator);
        cudaFree(deviceData);

        return a.toArray(String[]::new);
    }

}
