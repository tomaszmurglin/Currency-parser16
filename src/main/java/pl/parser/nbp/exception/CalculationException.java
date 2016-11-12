package pl.parser.nbp.exception;

public class CalculationException extends RuntimeException {

	public CalculationException(Exception e) {
		super(e);
	}
}
