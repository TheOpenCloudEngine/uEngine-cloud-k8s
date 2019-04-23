package com.example.template.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import org.springframework.stereotype.Service;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Container;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.util.Config;

@Service
public class PodService  {


    public ArrayList<LogMessageFormat> getLog(String kubehost, String kubetoken, String namespace, String name) throws IOException, ApiException{
    	ApiClient client = Config.fromToken(kubehost, kubetoken, false);
        CoreV1Api api = new CoreV1Api(client);
        
        String containerName = "";
		V1Pod v1pod = null;
		ArrayList<LogMessageFormat> al = new ArrayList<LogMessageFormat>();
        try {
        	v1pod = api.readNamespacedPod(name, namespace, null, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		if( v1pod != null ) {
			for (V1Container v1container : v1pod.getSpec().getContainers()) {
				if (!"istio-proxy".contentEquals(v1container.getName())) {
					containerName = v1container.getName();
				}
			}

			String log = api.readNamespacedPodLog(name, namespace, containerName, false, null, "true", false, null, null, false);

			ArrayList<SimpleDateFormat> formatterList = new ArrayList<SimpleDateFormat>();
			formatterList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES")));
			formatterList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", new Locale("es", "ES")));
			formatterList.add(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", new Locale("es", "ES")));
			formatterList.add(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", new Locale("es", "ES")));
			formatterList.add(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", new Locale("es", "ES")));
			formatterList.add(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS", Locale.ENGLISH));

			SimpleDateFormat defaultSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", new Locale("es", "ES"));
			SimpleDateFormat selectSimpleDateFormat = null;


			LogMessageFormat lmf = null;
			if( log != null ) {
				long times = 0;
				String[] loglines = log.split("\n");
				for (String lo : loglines) {

					String[] lineLogs = lo.split(" ");
					try {

						String dt = lineLogs[0] + " " + lineLogs[1];

						if (selectSimpleDateFormat == null) {

							for (SimpleDateFormat formatter : formatterList) {
								try {
									times = formatter.parse(dt).getTime();
								} catch (Exception e) {
									continue;
								}
								selectSimpleDateFormat = formatter;

								lmf = new LogMessageFormat();
								lmf.setDateTime(defaultSimpleDateFormat.format(times));
								lmf.setStatus(((lineLogs[2].equals("")) ? lineLogs[3] : lineLogs[2]));
								lmf.setMessage(lo.replace(dt, "").replace(lmf.getStatus(), ""));
								times = 0;
							}

						} else {
							try {
								times = selectSimpleDateFormat.parse(dt).getTime();
								if (lmf != null) {
									al.add(lmf);
								}

								lmf = new LogMessageFormat();
								lmf.setDateTime(defaultSimpleDateFormat.format(times));
								lmf.setStatus(((lineLogs[2].equals("")) ? lineLogs[3] : lineLogs[2]));
								lmf.setMessage(lo.replace(dt, "").replace(lmf.getStatus(), ""));
								times = 0;
							} catch (Exception e) {
								lmf.setMessage(((lmf.getMessage() == null) ? "" : lmf.getMessage()) + "\n " + lo);
							}

						}


						if (selectSimpleDateFormat == null) {
							if (lmf == null) {
								lmf = new LogMessageFormat();
							}

							lmf.setMessage(((lmf.getMessage() == null) ? "" : lmf.getMessage()) + lo + "\n ");
						}

					} catch (Exception e) {
					}
				}

				if (lmf != null) {
					al.add(lmf);
				}
			}

		}
        return al;
    }

	public List<String> getAllNamespaces(String kubehost, String kubetoken) throws IOException, ApiException{
		ApiClient client = Config.fromToken(kubehost, kubetoken, false);
		CoreV1Api api = new CoreV1Api(client);

		V1NamespaceList namespaceList = api.listNamespace(null,null,null,null,null,null,null,null,false);

		List<String> returnList = new ArrayList<String>();
		List<V1Namespace> list = namespaceList.getItems();
		for(V1Namespace item : list){
			returnList.add(item.getMetadata().getName());
		}

		return returnList;
	}
}
