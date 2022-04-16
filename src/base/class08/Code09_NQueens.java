package base.class08;

public class Code09_NQueens {

	public static int num1(int n) {
		if (n < 1) {
			return 0;
		}
		int[] record = new int[n];
		return process1(0, record, n);
	}

	// 潜台词：record[0...i-1] 的皇后，任何两个皇后一定都不共行，不共列、不公斜线
	// 目前来到了第 i 行
	// record[0...i-1] 表示之前的行，放了皇后的位置（列数）
	// n 代表一共有多少行
	// 返回值是：摆完所有的皇后合理的摆法有多少种
	public static int process1(int i, int[] record, int n) {
		if (i == n) { // 终止行
			return 1; // 找到了一个满足条件的例子
		}
		int res = 0;
		for (int j = 0; j < n; j++) { // 当前行在第 i 行，尝试第 i 行所有列 -> j
			if (isValid(record, i, j)) {
				record[i] = j;
				res += process1(i + 1, record, n);
			}
		}
		return res;
	}

	//  record[0...i-1] 需要检查，record[i...n] 不需要看
	// 返回第 i 行皇后放在第 j 列是否有效
	public static boolean isValid(int[] record, int i, int j) {
		for (int k = 0; k < i; k++) {
			if (j == record[k] || Math.abs(record[k] - j) == Math.abs(i - k)) { // 此时只需要检查列，和对角线（斜率的绝对值为1）
				return false;
			}
		}
		return true;
	}

	// 常数优化，位运算加速
	public static int num2(int n) {
		if (n < 1 || n > 32) { // 请不要超过 32 皇后
			return 0;
		}
		int upperLim = n == 32 ? -1 : (1 << n) - 1; // 几皇后问题，末尾就有几个1
		return process2(upperLim, 0, 0, 0);
	}

	public static int process2(int upperLim,
							   int colLim, // 列的限制，1的位置不能放皇后，0的位置可以
							   int leftDiaLim, // 左斜线的限制，1的位置不能放皇后，0的位置可以
							   int rightDiaLim) { // 右斜线的限制，1的位置不能放皇后，0的位置可以
		if (colLim == upperLim) { // base case 发现一种有效的
			return 1;
		}
		int pos = 0;
		int mostRightOne = 0;

        // 所有候选皇后的位置，都在 pos 上
		pos = upperLim & (~(colLim | leftDiaLim | rightDiaLim)); // ~ 取反 ,pos为所有可以放皇后的列的位为1

		int res = 0;
		while (pos != 0) {
			mostRightOne = pos & (~pos + 1); // 二进制提取最右侧的 1
			pos = pos - mostRightOne; // 在这个位置试皇后，剪掉，为零，试下一个最右侧 1 , 每一个可选皇后都去试
			res += process2(upperLim, colLim | mostRightOne, // 当前列的限制
					(leftDiaLim | mostRightOne) << 1, // 当前列限制左移一位
					(rightDiaLim | mostRightOne) >>> 1); // 当前列限制右移一位，>>>为无符号右移，在这种情况下与 >> 没有区别
		}
		return res;
	}

	public static void main(String[] args) {
		int n = 14;

		long start = System.currentTimeMillis();
		System.out.println(num2(n));
		long end = System.currentTimeMillis();
		System.out.println("cost time: " + (end - start) + "ms");

		start = System.currentTimeMillis();
		System.out.println(num1(n));
		end = System.currentTimeMillis();
		System.out.println("cost time: " + (end - start) + "ms");

	}
}
