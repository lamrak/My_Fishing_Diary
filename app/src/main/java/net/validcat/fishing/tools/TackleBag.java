package net.validcat.fishing.tools;

import java.util.Arrays;

/**
 * Created by Dobrunov on 09.02.2016.
 */
public class TackleBag {
    private int tackleArrInd = 0;
    private int[] selectedIdx;
    private String[] tackles;

    public TackleBag(String[] tackles) {
        this.tackles = tackles;
        selectedIdx = new int[tackles.length];
        Arrays.fill(selectedIdx, -1);
    }

    public void handle(int tackle) {
        if (!contains(tackle))
            selectedIdx[tackleArrInd++] = tackle;
        else remove(tackle);
    }

    private void remove(int tackle) {
        for (int i = 0; i < tackleArrInd; i++) {
            if (tackle == selectedIdx[i]) {
                for (int j = i; j < tackleArrInd; j++) {
                    if (j+1 == selectedIdx.length)
                        selectedIdx[j] = -1;
                    else selectedIdx[j] = selectedIdx[j + 1];
                }
                tackleArrInd--;
                break;
            }
        }
    }

    private boolean contains(int tackle) {
        for (int aSelectedIdx : selectedIdx)
            if (tackle == aSelectedIdx)
                return true;

        return false;
    }

    public String getSelectedTackles() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < tackleArrInd; i++) {
            String s1 = tackles[selectedIdx[i]].substring(0, 1).toUpperCase();
            sb.append(s1);
            sb.append(tackles[selectedIdx[i]].substring(1));
            if (tackleArrInd == 1)
                break;
            else if (i + 1 == tackleArrInd)
                sb.append(".");
            else sb.append(", ");
        }

        return sb.toString();
    }
}
