package warrrr.game;

public class Position {
    private int rowNo;
    private int columnNo;

    public Position(int rowNo, int columnNo) {
        this.rowNo = rowNo;
        this.columnNo = columnNo;
    }

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public int getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}

	public void change(int newRow, int newCol) {
		this.rowNo = newRow;
		this.columnNo = newCol;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;

        Position position = (Position) o;
        return rowNo == position.rowNo && columnNo == position.columnNo;
    }

    @Override
    public String toString() {
        return "@{" +
                "rowNo=" + rowNo +
                ", columnNo=" + columnNo +
                '}';
    }
}
