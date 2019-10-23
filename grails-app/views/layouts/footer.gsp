<footer class="footer">
	<div class="container-fluid">
		<div class="row">
			<div class="col text-left">
		     	
		    </div>
			<div class="col">
		    	<h6>
				<g:meta name="app.code"/> v<g:meta name="app.version"/>
				<br/>
				build with Grails <g:meta name="app.grails.version"/> - Java ${System.getProperty('java.version')}
				</h6>
				<asset:image src="/gandi-ssl.png" />
				
				<ul class="list-inline" style="margin-top:10px;">
					<g:if test="${ false }">
						<li class="list-inline-item">
							<a class="btn mb-1 btn-github" href="https://github.com/gelleouet/smarthome-application"><i class="align-middle fab fa-github"></i> Github</a>
						</li>
						<li class="list-inline-item">
							<a class="btn mb-1 btn-github" href="https://github.com/gelleouet/smarthome-application/wiki"><i class="align-middle fab fa-github"></i> Wiki</a>
						</li>
					</g:if>
					<li class="list-inline-item">
						<a class="btn mb-1 btn-twitter" href="${g.meta(name: 'app.twitter') }"><i class="align-middle fab fa-twitter"></i> Twitter</a>
					</li>
					<li class="list-inline-item">
						<a class="btn mb-1 btn-facebook" href="${g.meta(name: 'app.facebook') }"><i class="align-middle fab fa-facebook"></i> Facebook</a>
					</li>
					<li class="list-inline-item">
						<a class="btn mb-1 btn-instagram" href="${g.meta(name: 'app.instagram') }"><i class="align-middle fab fa-instagram"></i> Instagram</a>
					</li>
				</ul>
		    </div>
			<div class="col text-right">
		    	
		    </div>
		</div>
	</div>
	
</footer>