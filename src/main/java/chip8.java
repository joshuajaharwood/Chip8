import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Chip-8 emulator core class.
 * <p>
 * We will use integers instead of bytes to prevent overflows.
 * Memory won't be an issue at this scale.
 */
public class chip8 {
    // Should we just make all of these integers..? Java's fault, not mine...
    private int opcode; // Ex-short

    private final int[] memory;

    // V0 -> VE registers, 1 byte wide
    private final int[] V;

    // I = index register, PC = program counter
    private short I;
    private short pc;

    // Pixel states
    private final int[] gfx;

    // Count at 60Hz. When set above they count down to zero.
    private char delay_timer;
    private char sound_timer;

    // Stack & stack pointer
    private final short[] stack; // 16 levels of stack
    private short sp;

    // Chip8 has hex keypad, 0x0 to 0xF
    private final int[] key;

    /*
    Variable initialisation.
     */
    public chip8() {
        stack = new short[16];
        key = new int[16];
        gfx = new int[64 * 32];
        V = new int[16];
        memory = new int[4096];

        pc = 0x200;
        opcode = 0;
        I = 0;
        sp = 0;

        loadRom("pong.ch8");
    }

    public void loadRom(String path) {
        byte[] bytes = getBytes(path);
        assert bytes != null; // TODO: take out later
        int[] ints = convertToInt(bytes);

        setMemory(ints, 0x200);
        System.out.println("Halting.");
    }

    /**
     * Returns a byte array of the file provided in path.
     *
     * @param path The file to be read in.
     * @return A byte array of the file's contents.
     */
    private byte[] getBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert a byte array to an int array.
     * @param bytes A byte array.
     * @return An int array.
     */
    private int[] convertToInt(byte[] bytes) {
        int[] tmp = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            tmp[i] = Byte.toUnsignedInt(bytes[i]);
        }
        return tmp;
    }

    private void setMemory(int[] binary, int offset) {
        System.arraycopy(binary, 0, this.memory, offset, binary.length);
    }

    /*
    Will fetch, decode, execute and update timers.
     */
    public void cycle() {
        while (true) {
            System.out.println(fetch());
            System.out.println("---");
            pc++;
        }
    }

    /**
     * - Check PC
     * - Go to that address in memory
     * - Get instruction
     */
    private String fetch() {
        return Integer.toHexString(memory[pc]);
    }
}
