<footer class="footer">
	<div class="container-fluid">
		<div class="row">
			<div class="col-2 text-left">
		     	
		    </div>
			<div class="col">
		    	<h6><g:meta name="app.code"/> v<g:meta name="app.version"/></h6>
				<asset:image src="/gandi-ssl.png" />
				
				<ul class="list-inline" style="margin-top:10px;">
					<g:if test="${ grailsApplication.config.social.web }">
						<li class="list-inline-item">
							<a class="btn mb-1 btn-light" href="${ grailsApplication.config.social.web }" target="social"><asset:image src="logo.png" height="16px"/> Ecodo</a>
						</li>
					</g:if>
					<g:if test="${ grailsApplication.config.social.twitter }">
						<li class="list-inline-item">
							<a class="btn mb-1 btn-twitter" href="${ grailsApplication.config.social.twitter }" target="social"><i class="align-middle fab fa-twitter"></i> Twitter</a>
						</li>
					</g:if>
					<g:if test="${ grailsApplication.config.social.facebook }">
						<li class="list-inline-item">
							<a class="btn mb-1 btn-facebook" href="${ grailsApplication.config.social.facebook }" target="social"><i class="align-middle fab fa-facebook"></i> Facebook</a>
						</li>
					</g:if>
					<g:if test="${ grailsApplication.config.social.instagram }">
						<li class="list-inline-item">
							<a class="btn mb-1 btn-instagram" href="${ grailsApplication.config.social.instagram }" target="social"><i class="align-middle fab fa-instagram"></i> Instagram</a>
						</li>
					</g:if>
					
					<li class="list-inline-item">
						<a class="btn mb-1 btn-light" href="https://github.com/gelleouet/smarthome-application/tree/ecodo" target="social"><app:icon name="github"/> EUPL v1.2</a>
					</li>
					
				</ul>
		    </div>
			<div class="col-2 text-right">
		    	
		    </div>
		</div>
	</div>
	
</footer>