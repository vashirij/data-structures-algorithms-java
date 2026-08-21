import java.util.ArrayList;
import java.util.List;

class BrowserHistory {

    private List<String> history;
    private int current;
    private int last;

    public BrowserHistory(String homepage) {

        history = new ArrayList<>();

        history.add(homepage);

        current = 0;
        last = 0;
    }

    public void visit(String url) {

        current++;

        // If this position already exists,
        // overwrite the old forward history.
        if (current < history.size()) {
            history.set(current, url);
        } else {
            history.add(url);
        }

        // Visiting a new page clears all forward history.
        last = current;
    }

    public String back(int steps) {

        // Move back, but never before the homepage.
        current = Math.max(0, current - steps);

        return history.get(current);
    }

    public String forward(int steps) {

        // Move forward, but never beyond valid history.
        current = Math.min(last, current + steps);

        return history.get(current);
    }
}

/**
 * Your BrowserHistory object will be instantiated and called as such:
 * BrowserHistory obj = new BrowserHistory(homepage);
 * obj.visit(url);
 * String param_2 = obj.back(steps);
 * String param_3 = obj.forward(steps);
 */