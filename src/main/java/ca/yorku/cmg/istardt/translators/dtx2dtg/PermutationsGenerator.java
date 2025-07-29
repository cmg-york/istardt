package ca.yorku.cmg.istardt.translators.dtx2dtg;

import java.util.ArrayList;
import java.util.List;

public class PermutationsGenerator {

    private List<String> result = new ArrayList<>();

    public List<String> generatePermutations(ArrayList<String> input) {
        permute(input, 0);
        return result;
    }

    private void permute(List<String> list, int start) {
        if (start == list.size() - 1) {
            result.add(String.join(" : ", list));
            return;
        }

        for (int i = start; i < list.size(); i++) {
            swap(list, start, i);
            permute(list, start + 1);
            swap(list, start, i); // backtrack
        }
    }

    private void swap(List<String> list, int i, int j) {
        String temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
