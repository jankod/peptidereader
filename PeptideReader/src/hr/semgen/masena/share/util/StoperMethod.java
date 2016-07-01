package hr.semgen.masena.share.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.SimpleTimeLimiter;

public class StoperMethod {

	
	public static void main(String[] args) throws Exception {
		SimpleTimeLimiter t =  new SimpleTimeLimiter();
		String e = t.callWithTimeout(new Callable<String>() {

			public String call() throws Exception {
				return HeplerLongMethod.longMethod();
			}
		}, 1100, TimeUnit.MILLISECONDS, true);
		log.debug("Finish main "+ e);
 	}
	
	
	
	
	public static void main2(String[] args) throws InterruptedException {
		StoperMethod m = new StoperMethod();
		m.runAndStopAfter(new Runnable() {

			public void run() {

				HeplerLongMethod.longMethod();
				log.debug("custom method is finish");
			}
		}, 8 * 1000, "Custom method");

		log.debug("Cekam da zavrsi");
		m.waitForFinish();
		log.debug("Zavrsio sam main");
	}

	public void waitForFinish() {
		if (isFinish) {
			return;
		}
		while (true) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				log.debug("Interupt");
				return;
			}
			if (isFinish) {
				return;
			}
		}
	}

	private static final Logger log = LoggerFactory.getLogger(StoperMethod.class);
	private boolean isCanceled = false;
	private volatile boolean isFinish = false;

	public void runAndStopAfter(final Runnable r, final long milisecAfter, final String methodName) {

		ExecutorService e = Executors.newFixedThreadPool(1, new ThreadFactory() {

			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				t.setName("Method for cancel");
				t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

					public void uncaughtException(Thread t, Throwable e) {
						log.error("Thread " + t.getName(), e);
					}
				});
				return t;
			}
		});

		final Future<String> future = e.submit(new Callable<String>() {

			public String call() throws Exception {
				try {
					r.run();
					return "";
				} catch (Throwable t) {
					log.error("", t);
					return "";
				} finally {
					// log.debug("Zavrsila run metoda, isFinish true");
					isFinish = true;
				}
			};
		});

		Timer t = new Timer(true);
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!future.isDone()) {
					log.debug("Nije zavrsila metoda {} nakon {} msec, idem ga cancel-at", methodName, milisecAfter);
					isCanceled = true;
					future.cancel(true);
				}
			}
		}, milisecAfter);
	}

	/**
	 * Da li ga je ubio na kraju
	 * 
	 * @return
	 */
	public boolean isCanceled() {
		return isCanceled;
	}

	public boolean isFinish() {
		return isFinish;
	}
}

class HeplerLongMethod {
	private static final Logger log = LoggerFactory.getLogger(HeplerLongMethod.class);
	private static int w;
	private static int count;

	public static String longMethod() {
		log.debug("Start run method");
		int t =0;
		while (true) {
			if (Thread.interrupted()) {
				log.debug("Moram se cancelat..");
				break;
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				log.debug("Run: Moram se cancelat-interupted exception..");
				break;
			}

			// count++;
			if (Thread.interrupted()) {
				log.debug("Moram se cancelat..");
				break;
			}

			String[] sres = "sdfsdfsd".split("2343534264576567asdgdfdfstweafc<scy<dcefsaefsadfsdfsdfasdfasdfewwe");
			if (sres.length < 21e23) {
				w /= sres.length;
				String k = w + "SDFSDfsd";
				w *= k.length();
			}
			if (count % 1000 == 0) {
				log.debug("Radim " + count + "  ---" + w);
			}
			count++;
			
			if(t++ > 4) {
				break;
			}
			
		}
		return "SSS";
	}
}
